package io.github.rxcats.aws.s3

import org.springframework.boot.context.properties.ConfigurationProperties
import software.amazon.awssdk.regions.Region

@ConfigurationProperties(prefix = "app.aws.s3")
data class AwsS3Properties(
    val bucket: String,
    val region: Region = Region.AP_NORTHEAST_2,
    val url: String
)
