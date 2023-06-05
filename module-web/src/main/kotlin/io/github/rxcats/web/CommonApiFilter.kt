package io.github.rxcats.web

import io.github.rxcats.core.Json
import io.github.rxcats.core.loggerK
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.net.URLDecoder

class CommonApiFilter(
    private val enableLogBack: Boolean = false,
    private val logHandler: RemoteApiLogEventHandler = NoOpsRemoteApiLogEventHandler()
) : OncePerRequestFilter() {
    companion object {
        private val log by loggerK

        private val IGNORE_FILTER_URI_LIST = listOf("/ping", "/health")
    }

    private var ignoreRemoteLoggingUris: List<String> = emptyList()

    enum class Header(val value: String) {
        X_ELAPSED_MILLIS("x-elapsed-millis"),
        X_FORWARDED_FOR("x-forwarded-for")
    }

    fun addIgnoreRemoteLoggingUris(uris: List<String>) {
        ignoreRemoteLoggingUris = uris
    }

    private fun shouldIgnoreRemoteLogging(request: HttpServletRequest): Boolean {
        if (ignoreRemoteLoggingUris.isEmpty()) return false
        val path = request.requestURI.orEmpty()
        val find = ignoreRemoteLoggingUris.firstOrNull { path.contains(it) }
        return find != null
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI.orEmpty()
        val find = IGNORE_FILTER_URI_LIST.firstOrNull { path.contains(it) }
        return find != null
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response)
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain)
        }
    }

    private fun doFilterWrapped(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        filterChain: FilterChain
    ) {
        try {
            val requestTime = System.currentTimeMillis()

            filterChain.doFilter(request, response)

            if (!enableLogBack && logHandler is NoOpsRemoteApiLogEventHandler) return

            val req = getRequest(request)
            val res = getResponse(response)

            val elapsedMillis = System.currentTimeMillis() - requestTime

            addResponseHeader(response, elapsedMillis)

            if (enableLogBack) {
                writeLogback(req, res, elapsedMillis)
            }

            if (logHandler !is NoOpsRemoteApiLogEventHandler && !shouldIgnoreRemoteLogging(request)) {
                sendToRemoteLogging(req, res, elapsedMillis)
            }
        } finally {
            MDC.clear()
            response.copyBodyToResponse()
        }
    }

    private fun addResponseHeader(response: ContentCachingResponseWrapper, elapsedMillis: Long) {
        response.addHeader(Header.X_ELAPSED_MILLIS.value, elapsedMillis.toString())
    }

    private fun urlDecode(queryString: String): String = runCatching {
        URLDecoder.decode(queryString, Charsets.UTF_8)
    }.getOrDefault("")

    private fun getRequest(request: ContentCachingRequestWrapper): FilterRequestData {
        val uri: String = if (request.queryString == null) {
            request.requestURI
        } else {
            "%s?%s".format(request.requestURI, urlDecode(request.queryString))
        }

        val headers = request.headerNames
            .asSequence()
            .mapNotNull { name ->
                name to request.getHeaders(name).toList()
            }.toMap()

        val remoteAddr = getRemoteProxyIp(request)

        // application/json 인 경우에만 body 의 로그를 남기도록 함
        val content = if (isApplicationJson(request.contentType)) {
            getContentString(request.contentAsByteArray)
        } else {
            ""
        }

        return FilterRequestData(
            method = request.method,
            uri = uri,
            headers = headers,
            rawBody = content,
            remoteAddr = remoteAddr
        )
    }

    private fun getRemoteProxyIp(request: ContentCachingRequestWrapper): String {
        return request.getHeader(Header.X_FORWARDED_FOR.value)
            ?.split(",")
            ?.firstOrNull()
            ?.trim()
            ?: request.remoteAddr
    }

    private fun getResponse(response: ContentCachingResponseWrapper): FilterResponseData {
        // application/json 인 경우에만 body 의 로그를 남기도록 함
        val content = if (isApplicationJson(response.contentType)) {
            getContentString(response.contentAsByteArray)
        } else {
            ""
        }

        return FilterResponseData(
            status = response.status,
            rawBody = content
        )
    }

    private fun getContentString(content: ByteArray): String {
        if (content.isEmpty()) return ""
        return Json.readTree(content).toString()
    }

    private fun wrapRequest(request: HttpServletRequest): ContentCachingRequestWrapper {
        return if (request is ContentCachingRequestWrapper) request else ContentCachingRequestWrapper(request)
    }

    private fun wrapResponse(response: HttpServletResponse): ContentCachingResponseWrapper {
        return if (response is ContentCachingResponseWrapper) response else ContentCachingResponseWrapper(response)
    }

    private fun isApplicationJson(contentType: String?): Boolean {
        if (contentType.isNullOrBlank()) {
            return false
        }

        val result = runCatching {
            MediaType.parseMediaType(contentType)
        }

        if (result.isFailure) {
            return false
        }

        return result.getOrThrow() == MediaType.APPLICATION_JSON
    }

    private fun writeLogback(req: FilterRequestData, res: FilterResponseData, elapsedMillis: Long) {
        log.info("[{}] {}", req.method, req.uri)
        log.info("headers={}", req.headers)
        log.info("remoteAddr={}", req.remoteAddr)
        log.info("statusCode={}", res.status)
        log.info("elapsedMillis={}ms", elapsedMillis)
        log.info("requestBody={}", req.rawBody)
        log.info("responseBody={}", res.rawBody)
    }

    private fun sendToRemoteLogging(req: FilterRequestData, res: FilterResponseData, elapsedMillis: Long) {
        val data = linkedMapOf<String, Any>()

        data["uri"] = req.uri
        data["method"] = req.method
        data["headers"] = req.headers.toString()
        data["remoteAddr"] = req.remoteAddr
        data["statusCode"] = res.status
        data["elapsedMillis"] = elapsedMillis
        data["reqBody"] = req.rawBody
        data["resBody"] = res.rawBody

        logHandler.handle(data)
    }
}
