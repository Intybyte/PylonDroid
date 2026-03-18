package io.github.vaan.droid

import io.github.pylonmc.rebar.addon.RebarAddon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

@Suppress("unused")
class PylonDroid : JavaPlugin(), RebarAddon {

    // Called when our plugin is enabled
    override fun onEnable() {
        _instance = this

        // Every Pylon addon must call this BEFORE doing anything Pylon-related
        registerWithRebar()
        DroidInstructions.init()
        DroidItems.init()
        DroidPages.init()
        DroidBlocks.init()
    }

    override val javaPlugin: JavaPlugin = this

    override val languages: Set<Locale> = setOf(Locale.ENGLISH)

    override val material: Material = Material.DEAD_BUSH

    companion object {
        // Stores the instance of the addon (there's only ever one)
        private var _instance: PylonDroid? = null

        // The public-facing, non-null instance of the addon
        val instance: PylonDroid
            get() = _instance ?: throw IllegalStateException("Plugin is not initialized yet")

        fun key(str: String) = NamespacedKey(instance, str)
    }
}
