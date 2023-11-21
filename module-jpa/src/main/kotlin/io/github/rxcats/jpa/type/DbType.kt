package io.github.rxcats.jpa.type

enum class DbType(val dbname: String, val sharded: Boolean) {
    COMMON("commondb", false),
    USER("userdb", true),
    ;

    fun get(): String {
        if (sharded) {
            throw IllegalArgumentException("sharded db use get(shardNo) method")
        }

        return dbname
    }

    fun get(shardNo: Int): String {
        if (shardNo < 0) {
            throw IllegalArgumentException("shardNo must be greater than zero")
        }

        return dbname + shardNo
    }
}
