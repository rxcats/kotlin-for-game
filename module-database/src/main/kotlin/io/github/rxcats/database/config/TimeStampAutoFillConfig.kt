package io.github.rxcats.database.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZoneOffset

@Configuration(proxyBeanMethods = false)
class TimeStampAutoFillConfig : MetaObjectHandler {
    override fun insertFill(metaObject: MetaObject?) {
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime::class.java, LocalDateTime.now(ZoneOffset.UTC))
    }

    override fun updateFill(metaObject: MetaObject?) {
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime::class.java, LocalDateTime.now(ZoneOffset.UTC))
    }
}
