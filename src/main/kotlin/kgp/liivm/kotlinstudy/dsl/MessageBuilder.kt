package kgp.liivm.kotlinstudy.dsl

fun message(sender: String, content: String, block: Message.() -> Unit): Message {
    val message = Message(sender, content)
    message.block()
    return message
}
