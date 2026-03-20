package io.github.vaan.droid.data.serializers

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.util.setNullable
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.instructions.base.Instruction
import io.github.vaan.droid.instructions.base.Operation
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object OperationSerializer : PersistentDataType<PersistentDataContainer, Operation> {

    val instructionKey = PylonDroid.key("operation")
    val instructionType = RebarSerializers.KEYED.keyedTypeFrom(Instruction::class.java) {
        Instruction.REGISTRY.getOrThrow(it)
    }

    val argKey = PylonDroid.key("arguments")
    val argType = RebarSerializers.LIST.strings()

    val commentKey = PylonDroid.key("comment")

    override fun getPrimitiveType(): Class<PersistentDataContainer> = PersistentDataContainer::class.java

    override fun getComplexType(): Class<Operation> = Operation::class.java

    override fun toPrimitive(complex: Operation, context: PersistentDataAdapterContext): PersistentDataContainer {
        val pdc = context.newPersistentDataContainer()
        pdc.set(instructionKey, instructionType, complex.instruction)
        pdc.set(argKey, argType, complex.args.toList())
        pdc.setNullable(commentKey, RebarSerializers.STRING, complex.comment)
        return pdc
    }

    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): Operation {
        val instruction = primitive.get(instructionKey, instructionType)!!

        val args = primitive.get(argKey, argType)
            ?.toTypedArray()
            ?: emptyArray()

        val comment = primitive.get(commentKey, RebarSerializers.STRING)

        return Operation(instruction, args, comment)
    }
}