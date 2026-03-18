package io.github.vaan.droid.data

import io.github.vaan.droid.data.accessor.DataAccessor
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
    private val operations = arrayListOf<Operation>()

    constructor(
        static: StaticDroidData,
        registryValues: Map<DataRegistry, Valued<*>>,
        stack: List<Valued<*>>,
        error: Boolean,
        log: List<String>,
        operations: List<Operation>
    ) : this(static) {

        this.registryValues.putAll(registryValues)

        this.stack.clear()
        this.stack.addAll(stack.take(static.stackSize))

        this.error = error

        this.log.clear()
        this.log.addAll(log)

        this.operations.clear()
        this.operations.addAll(operations)
    }

    fun accessorOf(key: String) : DataAccessor? = DataAccessor.of(this, key)

    fun addLog(severity: String, message: String) {
        log.add("[$severity] $message") // todo: fix DOS 2.0, cap size or whatever
    }

    fun restart(): DynamicDroidData = DynamicDroidData(
        static,
        mapOf(),
        listOf(),
        false,
        this.log,
        this.operations
    )

    fun isEnd() : Boolean {
        val value = registryValues[DataRegistry.PC] as Valued.IntVal
        return value.value == (operations.size - 1)
    }

    fun getOperations() : List<Operation> {
        return operations.toList()
    }
}