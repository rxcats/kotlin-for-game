package io.github.rxcats.aws.dynamodb

import org.springframework.boot.context.properties.ConfigurationProperties
import software.amazon.awssdk.regions.Region

@ConfigurationProperties(prefix = "app.aws.dynamodb")
data class DynamoProperties(
    val url: String,
    val region: Region = Region.AP_NORTHEAST_2,
    val tableNamePrefix: String = ""
)
