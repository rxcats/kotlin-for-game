package io.github.rxcats.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

@SpringBootTest(classes = [CommonApiFilterTest.FilterTestConfig::class, CommonApiFilterTest.SampleController::class])
@AutoConfigureMockMvc
class CommonApiFilterTest {

    @EnableAutoConfiguration
    @Configuration(proxyBeanMethods = false)
    class FilterTestConfig {
        @Bean
        fun filter(): CommonApiFilter {
            return CommonApiFilter(enableLogBack = true)
        }
    }

    @Controller
    class SampleController {
        @ResponseBody
        @GetMapping("/hello", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
        fun hello(@RequestBody req: String): Map<String, String> {
            return mapOf(
                "message" to "ok"
            )
        }

        @ResponseBody
        @GetMapping("/text", consumes = [MediaType.TEXT_PLAIN_VALUE], produces = [MediaType.TEXT_PLAIN_VALUE])
        fun text(): String {
            return "ok"
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun commonApiFilterJsonContentTypeTest() {
        mockMvc.perform(
            get("/hello")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"userId":"user#1","reqId":"reqId"}""")
        ).andExpect(status().isOk)
            .andExpect(content().json("""{"message":"ok"}"""))
            .andDo(print())
    }

    @Test
    fun commonApiFilterTextContentTypeTest() {
        mockMvc.perform(
            get("/text?param=param1&param2=param2&param3=한글 파라미터")
                .contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isOk)
            .andExpect(content().string("ok"))
            .andDo(print())
    }

}
