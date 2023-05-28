package org.reckful.archive.twitter.server.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration(
    private val configurationProperties: OpenApiProperties
) {

    @Bean
    fun openApi(): OpenAPI {
        val info = configurationProperties.info
        return OpenAPI()
            .info(
                Info()
                    .title(info.title)
                    .description(
                        """
                        ${info.shortDescription}
                        
                        You can find this project's source code here: ${beautifyHtmlLink(info.sourceCodeLink)}
                    """.trimIndent()
                    )
            )
            .servers(
                configurationProperties.serverUrls.map {
                    Server().url(it)
                }
            )
    }

    private fun beautifyHtmlLink(link: String): String {
        val linkWithoutNoise = link
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
            .removeSuffix("/")

        return "<a href=\"$link\">$linkWithoutNoise</a>"
    }
}

@ConfigurationProperties(prefix = "openapi")
data class OpenApiProperties(
    val info: OpenApiInfo,
    val serverUrls: List<String> = emptyList()
)

data class OpenApiInfo(
    val title: String,
    val shortDescription: String,
    val sourceCodeLink: String,
)
