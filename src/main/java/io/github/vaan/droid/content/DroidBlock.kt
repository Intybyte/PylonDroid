package io.github.vaan.droid.content

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarTickingBlock
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.StaticDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.data.serializers.DynamicDroidDataSerializer
import io.github.vaan.droid.instructions.base.Operation
import io.github.vaan.droid.instructions.base.PisaAdd
import io.github.vaan.droid.instructions.base.PisaPrint
import io.github.vaan.droid.instructions.base.PisaSet
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer

class DroidBlock : RebarBlock, RebarTickingBlock {
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
            val dynamicTemp = ctx.item.persistentDataContainer.get(DYNAMIC_KEY, DynamicDroidDataSerializer)
            dynamic = dynamicTemp ?: DynamicDroidData(static)
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

        if (dynamic.error || dynamic.isEnd()) {
            dynamic = dynamic.restart()
            return
        }

        val pc = dynamic.registryValues[DataRegistry.PC]!!.value as Int

        val operations = listOf(
            Operation(PisaSet, arrayOf("A", "5")),
            Operation(PisaAdd, arrayOf("A", "A", "3")),
            Operation(PisaPrint, arrayOf("A"))
        )
        //val operations = dynamic.getOperations()
        if (pc >= operations.size) {
            dynamic.error = true
            dynamic.addLog("CRITICAL", "PC registry can't be greater than available operations")
            return
        }

        operations[pc].execute(dynamic)

        dynamic.registryValues[DataRegistry.PC] = Valued.IntVal(pc + 1)
    }

    override fun getDropItem(context: BlockBreakContext): ItemStack? {
        val item = defaultItem?.getItemStack() ?: return null
        item.editPersistentDataContainer { pdc ->
            pdc.set(DYNAMIC_KEY, DynamicDroidDataSerializer, dynamic)
        }

        return item
    }

    fun static() = StaticDroidData(key)

    companion object {
        val DYNAMIC_KEY = PylonDroid.key("dynamic_data")
        val STARTED_KEY = PylonDroid.key("started")
    }
}