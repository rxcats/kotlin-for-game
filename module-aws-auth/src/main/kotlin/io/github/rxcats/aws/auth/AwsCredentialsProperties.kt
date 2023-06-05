package io.github.rxcats.aws.auth

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.aws.auth")
data class AwsCredentialsProperties(
    val accessKeyId: String = "iam",
    val secretAccessKey: String = "iam",
)
