package io.github.vaan.droid.gui

import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.vaan.droid.content.DroidBlock
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.*
import xyz.xenondevs.invui.gui.SlotElement
import xyz.xenondevs.invui.inventory.Inventory
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.state.Property
import xyz.xenondevs.invui.window.Window

class InventoryItem(droid: DroidBlock) : DroidItem(
    droid,
    ItemStackBuilder.gui(Material.CHEST, "droid_inventory")
        .name(Component.translatable("pylondroid.gui.inventory"))
) {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        click: Click
    ) {
        Window.builder()
            .setTitle(Component.translatable("pylondroid.gui.inventory"))
            .setUpperGui(getGui())
            .build(player)
            .open()
    }

    private fun getGui() = PagedGui.guisBuilder()
            .setStructure(
                "< # # # d # # # >",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('<', GuiItems.pagePrevious())
            .addIngredient('>', GuiItems.pageNext())
            .addIngredient('d', ItemButton(droid.defaultItem!!.getItemStack()))
            .addIngredient('x', InvSupplier(droid.dynamic.inventory))

    @Suppress("UnstableApiUsage")
    class InvSupplier(val inventory: Inventory, val offset: Int = 0) : SlotElementSupplier {

        init {
            require(inventory.size > 0) { "Illegal inventory size: " + inventory.size }
            require(offset < inventory.size) { "Offset must be less than inventory size, but was " + offset + " for inventory of size " + inventory.size }
        }

        override fun generateSlotElements(slots: List<Slot>): List<SlotElement> {

            val elements = ArrayList<SlotElement>()
            for (i in slots.indices) {
                if (i < inventory.size) {
                    elements.add(SlotElement.InventoryLink(inventory, offset + i, EMPTY_BACKGROUND))
                } else {
                    elements.add(SlotElement.Item(Item.simple(BLOCKED)))
                }
            }

            return elements
        }

        companion object {
            val BLOCKED = ItemStackBuilder.gui(Material.RED_STAINED_GLASS_PANE, "blocked_slot")
            val EMPTY_BACKGROUND: Property<ItemProvider?> = Property.of(null)
        }
    }
}