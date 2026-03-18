package io.github.vaan.droid.gui

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.vaan.droid.content.DroidBlock
import org.bukkit.entity.Player
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider

abstract class DroidItem(val droid: DroidBlock, val stack: ItemStackBuilder) : AbstractItem() {
    override fun getItemProvider(viewer: Player): ItemProvider = stack
}