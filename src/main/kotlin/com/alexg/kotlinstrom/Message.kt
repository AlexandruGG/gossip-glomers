package com.alexg.kotlinstrom

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(val src: String, val dest: String, val body: Body) {
    fun reply(store: InMemoryStore): Message = body.reply(this, store)
}

@Serializable
sealed class Body {
    abstract fun reply(message: Message, store: InMemoryStore): Message
}

@Serializable
@SerialName("init")
data class Init(
    val nodeId: String,
    val nodeIds: List<String>,
    val msgId: Int,
) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = with(message.body as Init) {
        store.currentNode = nodeId

        message.copy(src = message.dest, dest = message.src, body = InitOk(inReplyTo = msgId))
    }
}

@Serializable
@SerialName("init_ok")
data class InitOk(val inReplyTo: Int) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = throw IllegalStateException("Received unexpected InitOk message!")
}

@Serializable
@SerialName("echo")
data class Echo(
    val echo: String,
    val msgId: Int,
) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = with(message.body as Echo) {
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
    override fun reply(message: Message, store: InMemoryStore) = throw IllegalStateException("Received unexpected EchoOk message!")
}

@Serializable
@SerialName("generate")
data class Generate(val msgId: Int) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = with(message.body as Generate) {
        message.copy(
            src = message.dest,
            dest = message.src,
            body = GenerateOk(id = "${store.currentNode}-$msgId", msgId = msgId + 1, inReplyTo = msgId),
        )
    }
}

@Serializable
@SerialName("generate_ok")
data class GenerateOk(
    val id: String,
    val msgId: Int,
    val inReplyTo: Int,
) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = throw IllegalStateException("Received unexpected GenerateOk message!")
}

@Serializable
@SerialName("topology")
data class Topology(
    val topology: Map<String, Set<String>>,
    val msgId: Int,
) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = with(message.body as Topology) {
        store.topology = topology

        message.copy(src = message.dest, dest = message.src, body = TopologyOk(msgId = msgId + 1, inReplyTo = msgId))
    }
}

@Serializable
@SerialName("topology_ok")
data class TopologyOk(
    val msgId: Int,
    val inReplyTo: Int,
) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = throw IllegalStateException("Received unexpected TopologyOk message!")
}

@Serializable
@SerialName("read")
data class Read(val msgId: Int) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = with(message.body as Read) {
        message.copy(
            src = message.dest,
            dest = message.src,
            body = ReadOk(messages = store.messages, msgId = msgId + 1, inReplyTo = msgId),
        )
    }
}

@Serializable
@SerialName("read_ok")
data class ReadOk(val messages: Set<Int>, val msgId: Int, val inReplyTo: Int) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = throw IllegalStateException("Received unexpected ReadOk message!")
}

@Serializable
@SerialName("broadcast")
data class Broadcast(
    val message: Int,
    val msgId: Int,
) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = with(message.body as Broadcast) {
        store.messages += this.message

        message.copy(src = message.dest, dest = message.src, body = BroadcastOk(msgId = msgId + 1, inReplyTo = msgId))
    }
}

@Serializable
@SerialName("broadcast_ok")
data class BroadcastOk(val msgId: Int, val inReplyTo: Int) : Body() {
    override fun reply(message: Message, store: InMemoryStore) = throw IllegalStateException("Received unexpected BroadcastOk message!")
}
