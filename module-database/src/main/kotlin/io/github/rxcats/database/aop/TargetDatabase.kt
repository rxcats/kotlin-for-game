package io.github.rxcats.database.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TargetDatabase (
    val db: String = "",
    val useTransaction: Boolean = false,
)
