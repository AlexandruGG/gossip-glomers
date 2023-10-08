package com.alexg.kotlinstrom

import org.springframework.stereotype.Component

@Component
class InMemoryStore {
    lateinit var currentNode: String
    lateinit var topology: Map<String, Set<String>>
    val messages: MutableSet<Int> = mutableSetOf()
}
