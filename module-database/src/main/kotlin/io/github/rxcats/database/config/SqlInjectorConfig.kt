package io.github.rxcats.database.config

import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector
import com.baomidou.mybatisplus.core.metadata.TableInfo
import io.github.rxcats.mybatisplus.methods.BulkInsert
import io.github.rxcats.mybatisplus.methods.Upsert
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class SqlInjectorConfig : DefaultSqlInjector() {
    override fun getMethodList(mapperClass: Class<*>?, tableInfo: TableInfo): List<AbstractMethod> {
        val methodList = super.getMethodList(mapperClass, tableInfo)
        methodList.add(BulkInsert())
        methodList.add(Upsert())
        return methodList
    }
}
