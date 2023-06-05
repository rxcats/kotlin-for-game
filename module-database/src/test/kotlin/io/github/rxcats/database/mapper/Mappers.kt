package io.github.rxcats.database.mapper

import io.github.rxcats.mybatisplus.mapper.CrudMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface CommonUserMapper : CrudMapper<CommonUser>

@Mapper
interface UserMapper : CrudMapper<User>
