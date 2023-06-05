package io.github.rxcats.aws.s3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [AwsS3ClientConfig::class, AwsS3Client::class])
class AwsS3ClientTest {

    @Autowired
    private lateinit var client: AwsS3Client

    @Test
    fun contextLoad() {
        assertThat(client).isNotNull()
    }

}
