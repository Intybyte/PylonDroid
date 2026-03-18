package io.github.vaan.droid.data

import io.github.pylonmc.rebar.config.Settings
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import org.bukkit.NamespacedKey

/**
 * Data that doesn't change after ticking or whatever happens
 * beside config change
 */
data class StaticDroidData(
    val key: NamespacedKey,
    val movingSpeed: Int,
    val reach: Int,
    val damage: Int,
    val stackSize: Int,
    val tickSpeed: Int
) {
    constructor(key: NamespacedKey) : this(
        key,
        Settings.get(key).getOrThrow("moving-speed", ConfigAdapter.INTEGER),
        Settings.get(key).getOrThrow("reach", ConfigAdapter.INTEGER),
        Settings.get(key).getOrThrow("damage", ConfigAdapter.INTEGER),
        Settings.get(key).getOrThrow("stackSize", ConfigAdapter.INTEGER),
        Settings.get(key).getOrThrow("tickSpeed", ConfigAdapter.INTEGER)
    )
}