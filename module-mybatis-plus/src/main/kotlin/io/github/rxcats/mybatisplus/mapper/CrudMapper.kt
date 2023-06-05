package io.github.rxcats.mybatisplus.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Param

interface CrudMapper<T> : BaseMapper<T> {
    /**
     * To auto populate, the @Param("xx") xx parameter name must be one of 3 `list`, `collection`, `arrays`
     */
    fun bulkInsert(@Param("list") dataList: List<T>): Int

    fun upsert(data: T): Int

}
