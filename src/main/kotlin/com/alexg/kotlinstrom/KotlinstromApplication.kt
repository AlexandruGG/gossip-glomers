package com.alexg.kotlinstrom

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinstromApplication(private val handler: Handler) : CommandLineRunner {
    override fun run(vararg args: String) = handler.run()
}

fun main(args: Array<String>) {
    runApplication<KotlinstromApplication>(*args)
}
