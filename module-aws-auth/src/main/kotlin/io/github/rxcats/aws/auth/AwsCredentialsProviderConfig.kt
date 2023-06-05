package io.github.rxcats.aws.auth

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

@EnableConfigurationProperties(AwsCredentialsProperties::class)
@Configuration(proxyBeanMethods = false)
class AwsCredentialsProviderConfig {
    @Profile("local")
    @ConditionalOnMissingBean
    @Bean
    fun awsStaticCredentialsProvider(properties: AwsCredentialsProperties): AwsCredentialsProvider {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(properties.accessKeyId, properties.secretAccessKey))
    }

    @Profile("!local")
    @ConditionalOnMissingBean
    @Bean
    fun awsInstanceProfileCredentialsProvider(): AwsCredentialsProvider {
        return InstanceProfileCredentialsProvider.create()
    }

}
