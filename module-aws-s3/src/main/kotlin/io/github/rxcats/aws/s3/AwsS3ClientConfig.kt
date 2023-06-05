package io.github.rxcats.aws.s3

import io.github.rxcats.aws.auth.AwsCredentialsProviderConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.s3.S3Client

@Import(AwsCredentialsProviderConfig::class)
@EnableConfigurationProperties(AwsS3Properties::class)
@Configuration(proxyBeanMethods = false)
class AwsS3ClientConfig {
    @Bean
    fun s3ClientV2(properties: AwsS3Properties, awsCredentialsProvider: AwsCredentialsProvider): S3Client {
        return S3Client.builder()
            .region(properties.region)
            .credentialsProvider(awsCredentialsProvider)
            .build()
    }
}
