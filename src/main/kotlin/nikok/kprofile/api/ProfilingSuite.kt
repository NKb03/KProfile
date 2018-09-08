package nikok.kprofile.api

import nikok.kprofile.impl.ProfileBodyImpl

abstract class ProfilingSuite(
    private val topic: String,
    private vararg val tags: Tag,
    private val body: ProfileBody.() -> Unit
) {
    fun run() {
        val pb = ProfileBodyImpl(topic, tags.asList())
        pb.body()
        pb.write()
    }
}