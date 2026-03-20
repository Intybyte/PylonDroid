package io.github.vaan.droid.data

import io.github.vaan.droid.data.accessor.DataAccessor
import io.github.vaan.droid.instructions.base.Operation
import io.github.vaan.droid.instructions.jump.PisaLabel
import org.bukkit.Bukkit
import java.util.*

class DynamicDroidData(val static: StaticDroidData) {
    val registryValues = EnumMap<DataRegistry, Valued<*>>(DataRegistry::class.java).also { map ->
        for (key in DataRegistry.entries) {
            if (key.strictClassType == Valued.IntVal::class.java) {
                map[key] = Valued.IntVal(0)
            } else if (key == DataRegistry.ID) {
                map[key] = Valued.UUIDVal(UUID(0, 0))
            } else if (key.strictClassType == null) {
                map[key] = Valued.EmptyVal
            } else {
                error("Registry ${key.name} not initialized properly")
            }
        }
    }

    val stack: ArrayList<Valued<*>?> = ArrayList<Valued<*>?>(static.stackSize).apply {
        repeat(static.stackSize) {
            this.add(Valued.EmptyVal)
        }
    }

    var error: Boolean = false
    val log = LinkedList<String>()

    private val operations = arrayListOf<Operation>()
    val labels = HashMap<String, Int>()

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
        val diff = static.stackSize - stack.size
        repeat(diff) {
            this.stack.add(Valued.EmptyVal)
        }

        this.error = error

        this.log.clear()
        this.log.addAll(log)

        setOperations(operations)
    }

    fun accessorOf(key: String) : DataAccessor? = DataAccessor.of(this, key)

    fun addLog(severity: String, message: String) {
        Bukkit.broadcastMessage("LOG [$severity] $message")
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

    fun getOperations() : List<Operation> {
        return operations.toList()
    }

    fun setOperations(ops: Collection<Operation>) {
        operations.clear()
        operations.addAll(ops)
        labels.clear()
        labels.putAll(operations.mapIndexed { i, op ->
            if (op.instruction == PisaLabel && op.args.size == 1) {
                op.args[0] to i
            } else {
                null
            }
        }.filterNotNull())
    }
}