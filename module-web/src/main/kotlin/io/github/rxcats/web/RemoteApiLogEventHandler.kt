package io.github.rxcats.web

/**
 * RemoteApiLogHandler 를 구현하여 bean 으로 등록하게 되면
 * CommonApiFilter 에서 handle method 를 호출하도록 하는 역할입니다.
 */
interface RemoteApiLogEventHandler {
    fun handle(data: Map<String, Any>)
}
