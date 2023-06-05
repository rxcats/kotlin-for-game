package io.github.rxcats.web

data class FilterRequestData(
    val method: String,
    val uri: String,
    val headers: Map<String, List<String>> = emptyMap(),
    val rawBody: String = "",
    val remoteAddr: String = ""
)

data class FilterResponseData(
    val status: Int = 0,
    var rawBody: String = ""
)
