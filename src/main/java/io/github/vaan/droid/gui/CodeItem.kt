package io.github.vaan.droid.gui

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.vaan.droid.content.DroidBlock
import io.github.vaan.droid.instructions.base.Operation
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.dialog.DialogResponseView
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback
import io.papermc.paper.registry.data.dialog.input.DialogInput
import io.papermc.paper.registry.data.dialog.input.TextDialogInput
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
class CodeItem(droid: DroidBlock) : DroidItem(
    droid,
    ItemStackBuilder.gui(Material.CLOCK, "droid_code")
        .name(Component.translatable("pylondroid.gui.code"))
), DialogActionCallback {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        click: Click
    ) {
        val operationTexts = droid.dynamic.getOperations().joinToString("\n") { it.toString() }

        val dialog = Dialog.create { factory ->
            factory.empty()
                .base(
                    DialogBase.builder(
                        GlobalTranslator.render(Component.translatable("rebar.message.pipe.cannot_place_here"), player.locale()))
                        .inputs(listOf(
                            DialogInput.text(
                                "code",
                                1024,
                                Component.empty(),
                                false,
                                operationTexts,
                                Int.MAX_VALUE,
                                TextDialogInput.MultilineOptions.create(
                                    128,
                                    null
                                )
                            )
                        ))
                        .canCloseWithEscape(false)
                        .build()
                )
                .type(DialogType.notice(
                    ActionButton.create(
                        Component.text("CLOSE"),
                        null,
                        128,
                        DialogAction.customClick(this, ClickCallback.Options.builder().build())
                    )
                ))
        }

        player.showDialog(dialog)
    }

    override fun accept(response: DialogResponseView, audience: Audience) {
        val string = response.getText("code") ?: return
        val list = string
            .lines()
            .stream()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { Operation.of(it) }
            .toList()

        if (list.any { it == null } ) {
            audience.sendMessage(Component.text("Error"))
            return
        }

        audience.sendMessage(Component.text("Successful update"))
        droid.dynamic.operations.clear()
        droid.dynamic.operations.addAll(list as Collection<out Operation>)
    }
}