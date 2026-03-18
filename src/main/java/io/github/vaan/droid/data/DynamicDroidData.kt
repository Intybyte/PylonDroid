package io.github.vaan.droid.data

import io.github.vaan.droid.data.accessor.DataAccessor
import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.instructions.base.Operation
import java.util.*
import kotlin.collections.listOf
import kotlin.collections.mapOf

class DynamicDroidData(val static: StaticDroidData) {
    val registryValues = EnumMap<DataRegistry, Valued<*>>(DataRegistry::class.java).also { map ->
        map[DataRegistry.PC] = Valued.IntVal(0)
        map[DataRegistry.SS] = Valued.IntVal(0)
    }

    val stack: ArrayList<Valued<*>?> = ArrayList(static.stackSize)
    var error: Boolean = false
    val log = LinkedList<String>()
    val instructions = arrayListOf<Operation>()

    constructor(
        static: StaticDroidData,
        registryValues: Map<DataRegistry, Valued<*>>,
        stack: List<Valued<*>>,
        error: Boolean,
        log: List<String>,
        instructions: List<Operation>
    ) : this(static) {

        this.registryValues.putAll(registryValues)

        this.stack.clear()
        this.stack.addAll(stack.take(static.stackSize))

        this.error = error

        this.log.clear()
        this.log.addAll(log)

        this.instructions.clear()
        this.instructions.addAll(instructions)
    }

    fun accessorOf(key: String) : DataAccessor? = DataAccessor.of(this, key)

    fun addLog(severity: String, message: String) {
        log.add("[$severity] $message")
    }

    fun restart(): DynamicDroidData = DynamicDroidData(
        static,
        mapOf(),
        listOf(),
        false,
        this.log,
        this.instructions
    )

    fun isEnd() : Boolean {
        val value = registryValues[DataRegistry.PC] as Valued.IntVal
        return value.value == (instructions.size - 1)
    }
}