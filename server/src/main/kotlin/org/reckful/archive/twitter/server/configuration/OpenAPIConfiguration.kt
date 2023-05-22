package org.reckful.archive.twitter.server.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Reckful's Twitter Archive")
                    .description(
                        """
                        API endpoints for querying tweets of or related to Byron (Reckful) Bernstein.
                        
                        You can find this project on GitHub: <a href="https://github.com/ReckfulArchive/twitter">github.com/ReckfulArchive/twitter</a>
                    """.trimIndent()
                    )
            )
    }
}
