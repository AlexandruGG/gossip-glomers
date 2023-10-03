package com.alexg.kotlinstrom

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(val src: String, val dest: String, val body: Body) {
    fun reply(): Message = body.reply(this)
}

@Serializable
sealed class Body {
    abstract fun reply(message: Message): Message
}

@Serializable
@SerialName("init")
data class Init(
    val nodeId: String,
    val nodeIds: List<String>,
    val msgId: Int,
) : Body() {
    override fun reply(message: Message) = with(message.body as Init) {
        message.copy(src = message.dest, dest = message.src, body = InitOk(inReplyTo = msgId))
    }
}

@Serializable
@SerialName("init_ok")
data class InitOk(val inReplyTo: Int) : Body() {
    override fun reply(message: Message) = throw IllegalStateException("Received unexpected InitOk message!")
}

@Serializable
@SerialName("echo")
data class Echo(
    val echo: String,
    val msgId: Int,
) : Body() {
    override fun reply(message: Message) = with(message.body as Echo) {
        message.copy(src = message.dest, dest = message.src, body = EchoOk(echo = echo, msgId = msgId + 1, inReplyTo = msgId))
    }
}

@Serializable
@SerialName("echo_ok")
data class EchoOk(
    val echo: String,
    val msgId: Int,
    val inReplyTo: Int,
) : Body() {
    override fun reply(message: Message) = throw IllegalStateException("Received unexpected EchoOk message!")
}
