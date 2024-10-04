package kgp.liivm.kotlinstudy.dsl

class Message(
    val sender: String,
    val content: String
) {
    var receiver: String = ""
    var read: Boolean = false
}
