package com.alexg.kotlinstrom

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OptIn(ExperimentalSerializationApi::class)
class JsonConfig {

    @Bean
    fun buildJson(): Json = Json {
        ignoreUnknownKeys = true
        decodeEnumsCaseInsensitive = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }
}
