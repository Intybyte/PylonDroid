package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DynamicDroidData
import org.bukkit.NamespacedKey

object PisaComment : PisaBase(";", "Comment") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        
    }

    override fun getKey(): NamespacedKey = PylonDroid.key("comment")
}