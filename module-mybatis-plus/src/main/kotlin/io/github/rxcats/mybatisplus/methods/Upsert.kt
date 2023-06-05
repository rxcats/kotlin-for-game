package io.github.rxcats.mybatisplus.methods

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator
import org.apache.ibatis.executor.keygen.KeyGenerator
import org.apache.ibatis.executor.keygen.NoKeyGenerator
import org.apache.ibatis.mapping.MappedStatement

/**
 * Create SQL Script
 *
 * INSERT INTO tableName
 *      (id,name)
 * VALUES
 *      (id,name)
 * ON DUPLICATE KEY UPDATE
 *      name = name
 */
class Upsert : AbstractMethod("upsert") {
    override fun injectMappedStatement(
        mapperClass: Class<*>?, modelClass: Class<*>?,
        tableInfo: TableInfo
    ): MappedStatement {
        val sql = "<script>INSERT INTO %s %s VALUES %s ON DUPLICATE KEY UPDATE %s</script>"
        val fieldSql = getFieldSql(tableInfo)
        val valueSql = getValueSql(tableInfo)
        val afterValueSql = getAfterValueSql(tableInfo)
        val sqlResult = sql.format(tableInfo.tableName, fieldSql, valueSql, afterValueSql)
        println(sqlResult)
        val sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass)

        var keyGenerator: KeyGenerator = NoKeyGenerator.INSTANCE
        var keyProperty: String? = null
        var keyColumn: String? = null

        if (tableInfo.keyProperty.isNotBlank()) {
            if (tableInfo.idType == IdType.AUTO) {
                keyGenerator = Jdbc3KeyGenerator.INSTANCE
                keyProperty = tableInfo.keyProperty
                keyColumn = SqlInjectionUtils.removeEscapeCharacter(tableInfo.keyColumn)
            } else if (tableInfo.keySequence != null) {
                keyGenerator = TableInfoHelper.genKeyGenerator(methodName, tableInfo, builderAssistant)
                keyProperty = tableInfo.keyProperty
                keyColumn = tableInfo.keyColumn
            }
        }

        return this.addInsertMappedStatement(
            mapperClass, modelClass,
            methodName, sqlSource, keyGenerator,
            keyProperty, keyColumn
        )
    }

    private fun getFieldSql(tableInfo: TableInfo): String = "(${tableInfo.allSqlSelect})"

    private fun getValueSql(tableInfo: TableInfo): String {
        val sb = StringBuilder("(")

        if (tableInfo.havePK()) {
            sb.append("#{")
            sb.append(tableInfo.keyProperty).append("},")
        }

        var isNotFirst = false
        tableInfo.fieldList.forEach { field: TableFieldInfo ->
            if (isNotFirst) {
                sb.append(",")
            }
            sb.append("#{").append(field.property)
            field.typeHandler?.let { handler ->
                sb.append(",").append("typeHandler=").append(handler.name)
            }
            sb.append("}")
            isNotFirst = true
        }
        return sb.append(")").toString()
    }

    private fun getAfterValueSql(tableInfo: TableInfo): String {
        val sb = StringBuilder()
        var isNotFirst = false
        tableInfo.fieldList.forEach { field: TableFieldInfo ->
            if (isNotFirst) {
                sb.append(",")
            }
            sb.append(field.column).append(" = ").append("#{").append(field.property)
            field.typeHandler?.let { handler ->
                sb.append(",").append("typeHandler=").append(handler.name)
            }
            sb.append("}")
            isNotFirst = true
        }
        return sb.toString()
    }
}
