package com.alexg.kotlinstrom

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class Handler(private val json: Json) {

    fun run() {
        generateSequence(::readln).forEach {
            val inbound = decode(it)
            val outbound = inbound.reply()

            send(outbound)
        }
    }

    private fun decode(inbound: String) = try {
        json.decodeFromString<Message>(inbound).also { log("Received inbound: $inbound") }
    } catch (e: Exception) {
        throw e.also { log("Failed to decode message: $inbound. Exception: $e") }
    }

    private fun send(outbound: Message) {
        val encoded = try {
            json.encodeToString(outbound).also { log("Sending outbound: $it") }
        } catch (e: Exception) {
            throw e.also { log("Failed to encode message: $outbound. Exception: $e") }
        }

        println(encoded)
    }

    private fun log(message: String) {
        System.err.println(message)
    }
}
