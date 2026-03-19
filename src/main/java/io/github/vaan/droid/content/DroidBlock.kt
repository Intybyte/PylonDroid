package io.github.vaan.droid.content

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarGuiBlock
import io.github.pylonmc.rebar.block.base.RebarTickingBlock
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.StaticDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.data.serializers.DynamicDroidDataSerializer
import io.github.vaan.droid.gui.CodeItem
import io.github.vaan.droid.gui.LogItem
import io.github.vaan.droid.gui.StartupItem
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import xyz.xenondevs.invui.gui.Gui

class DroidBlock : RebarBlock, RebarTickingBlock, RebarGuiBlock {
    val static: StaticDroidData
    var dynamic: DynamicDroidData

    var started: Boolean

    init {
        static = static()
        setTickInterval(static.tickSpeed)
    }

    @Suppress("unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc) {
        dynamic = pdc.get(DYNAMIC_KEY, DynamicDroidDataSerializer)!!
        started = pdc.get(STARTED_KEY, RebarSerializers.BOOLEAN)!!
    }

    @Suppress("unused")
    constructor(block: Block, ctx: BlockCreateContext) : super(block, ctx) {
        // bring over all info
        if (ctx is BlockCreateContext.PlayerPlace) {
            val view = ctx.item.persistentDataContainer
            dynamic = if (view.has(DYNAMIC_KEY)) {
                view.get(DYNAMIC_KEY, DynamicDroidDataSerializer)!!
            } else {
                DynamicDroidData(static)
            }
        } else {
            dynamic = DynamicDroidData(static)
        }

        started = false
    }

    override fun write(pdc: PersistentDataContainer) {
        pdc.set(DYNAMIC_KEY, DynamicDroidDataSerializer, dynamic)
        pdc.set(STARTED_KEY, RebarSerializers.BOOLEAN, started)
    }

    override fun tick() {
        if (!started) return

        val operations = dynamic.getOperations()
        if (operations.isEmpty()) return

        val pc = dynamic.registryValues[DataRegistry.PC]!!.value as Int

        if (pc >= operations.size) {
            restart()
            dynamic.addLog("CRITICAL", "PC registry can't be greater than available operations")
            return
        }

        val op = operations[pc]
        if ((DataRegistry.DBG.get(dynamic) as Valued.IntVal).value != 0) {
            dynamic.addLog("DEBUG", "Executing $op at line $pc")
        }
        op.execute(dynamic)

        dynamic.registryValues[DataRegistry.PC] = Valued.IntVal(pc + 1)

        if (dynamic.error) {
            restartWithError()
            return
        }

        if (operations.size == pc + 1) {
            restart()
            return
        }
    }

    fun restart() {
        dynamic = dynamic.restart()
    }

    fun restartWithError() {
        restart()
        started = false
        block.world.playSound(ERROR_SOUND, net.kyori.adventure.sound.Sound.Emitter.self())
    }

    override fun getDropItem(context: BlockBreakContext): ItemStack? {
        val item = defaultItem?.getItemStack() ?: return null
        item.editPersistentDataContainer { pdc ->
            pdc.set(DYNAMIC_KEY, DynamicDroidDataSerializer, dynamic)
        }

        return item
    }

    fun static() = StaticDroidData(key)

    override fun createGui(): Gui = Gui.builder()
        .setStructure(
            "# # # # # # # # #",
            "# o # c # l # # #",
            "# # # # # # # # #"
        )
        .addIngredient('#', GuiItems.background())
        .addIngredient('o', StartupItem(this))
        .addIngredient('c', CodeItem(this))
        .addIngredient('l', LogItem(this))
        .build()

    companion object {
        val DYNAMIC_KEY = PylonDroid.key("dynamic_data")
        val STARTED_KEY = PylonDroid.key("started")

        val ERROR_SOUND = net.kyori.adventure.sound.Sound.sound {
            it.type(Sound.BLOCK_ANVIL_BREAK)
            it.source(net.kyori.adventure.sound.Sound.Source.AMBIENT)
            it.pitch(0.2f)
        }
    }
}