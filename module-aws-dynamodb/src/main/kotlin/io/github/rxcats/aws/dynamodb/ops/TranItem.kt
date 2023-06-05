package io.github.rxcats.aws.dynamodb.ops

@JvmInline
value class TranLoadItem<T : Any>(
    val item: T
)

data class TranWriteItem<T : Any>(
    val item: T,
    val mode: TranWriteMode
)

enum class TranWriteMode {
    DELETE, UPDATE
}

@JvmInline
value class TranLoadResult(
    val results: List<Any?> = emptyList()
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(index: Int): T? {
        return results[index] as? T
    }

    override fun toString(): String {
        return "TranLoadResult(results=$results)"
    }
}
