package io.github.vaan.droid.gui

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.vaan.droid.content.DroidBlock
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.dialog.DialogResponseView
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.translation.GlobalTranslator
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import xyz.xenondevs.invui.Click


@Suppress("UnstableApiUsage")
class LogItem(droid: DroidBlock) : DroidItem(
    droid,
    ItemStackBuilder.gui(Material.PAPER, "droid_log")
        .name(Component.translatable("pylondroid.gui.log"))
) {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        click: Click
    ) {
        val logs = droid.dynamic.log.joinToString("\n")

        val dialog = Dialog.create { factory ->
            factory.empty()
                .base(
                    DialogBase.builder(
                        GlobalTranslator.render(Component.translatable("rebar.message.pipe.cannot_place_here"), player.locale()))
                        .body(
                            listOf(DialogBody.plainMessage(
                                    Component.text(logs),
                                    512
                                )
                            )
                        )
                        .canCloseWithEscape(true)
                        .build()
                )
                .type(DialogType.multiAction(
                    listOf(
                        ActionButton.create(
                            Component.text("CLOSE"),
                            null,
                            128,
                            DialogAction.customClick(this::empty, ClickCallback.Options.builder().build())
                        ),
                        ActionButton.create(
                            Component.text("CLEAR"),
                            null,
                            128,
                            DialogAction.customClick(this::resetLog, ClickCallback.Options.builder().build())
                        )
                    )).build()
                )

        }

        player.showDialog(dialog)
    }

    fun resetLog(response: DialogResponseView, audience: Audience) {
        droid.dynamic.log.clear()
    }

    fun empty(response: DialogResponseView, audience: Audience) {}
}