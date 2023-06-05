package io.github.rxcats.core.spring

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@SpringBootTest(classes = [ApplicationContextProviderInitializer::class, ApplicationContextProviderTest.ApplicationContextProviderTestConfig::class])
class ApplicationContextProviderTest {

    class TestBean

    @TestConfiguration(proxyBeanMethods = false)
    class ApplicationContextProviderTestConfig {
        @Bean
        fun testBean(): TestBean = TestBean()
    }

    @Autowired
    private lateinit var testBean: TestBean

    @Test
    fun applicationContextProvider() {
        assertThat(ApplicationContextProvider()).isNotNull
    }

    @Test
    fun testBeanTest() {
        val testBeanByCtxProviderInitializer = ApplicationContextProvider.getBean<TestBean>()
        assertThat(testBean).isSameAs(testBeanByCtxProviderInitializer)
    }

}
