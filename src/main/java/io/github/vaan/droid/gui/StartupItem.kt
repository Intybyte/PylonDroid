package io.github.vaan.droid.gui

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.vaan.droid.content.DroidBlock
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider

class StartupItem(val droid: DroidBlock) : AbstractItem() {
    override fun getItemProvider(viewer: Player): ItemProvider {
        return if (droid.started) {
            ON
        } else {
            OFF
        }
    }

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        click: Click
    ) {
        droid.started = !droid.started
        notifyWindows()
    }

    companion object {
        val ON = ItemStackBuilder.gui(Material.LIME_STAINED_GLASS_PANE, "droid_on")
            .name(Component.translatable("pylondroid.gui.on"))
        val OFF = ItemStackBuilder.gui(Material.RED_STAINED_GLASS_PANE, "droid_off")
            .name(Component.translatable("pylondroid.gui.off"))
    }
}