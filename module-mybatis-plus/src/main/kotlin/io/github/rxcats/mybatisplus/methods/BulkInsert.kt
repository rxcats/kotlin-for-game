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
 *   <foreach collection="dataList" item="data" open="(" close=")" index="index" separator="),(">
 *       #{data.id},#{data.name}
 *   </foreach>
 */
class BulkInsert : AbstractMethod("bulkInsert") {
    override fun injectMappedStatement(
        mapperClass: Class<*>?, modelClass: Class<*>?,
        tableInfo: TableInfo
    ): MappedStatement {
        val sql = "<script>INSERT INTO %s %s VALUES %s</script>"
        val fieldSql = getFieldSql(tableInfo)
        val valueSql = getValueSql(tableInfo)
        val sqlResult = sql.format(tableInfo.tableName, fieldSql, valueSql)
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
        val sb = StringBuilder("<foreach collection=\"list\" item=\"data\" open=\"(\" close=\")\" index=\"index\" separator=\"),(\">")

        if (tableInfo.havePK()) {
            sb.append("#{data.")
            sb.append(tableInfo.keyProperty).append("},")
        }

        var isNotFirst = false
        tableInfo.fieldList.forEach { field: TableFieldInfo ->
            if (isNotFirst) {
                sb.append(",")
            }
            sb.append("#{data.").append(field.property)
            field.typeHandler?.let { handler ->
                sb.append(",").append("typeHandler=").append(handler.name)
            }
            sb.append("}")
            isNotFirst = true
        }
        sb.append("</foreach>")
        return sb.toString()
    }
}
