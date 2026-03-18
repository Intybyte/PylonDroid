package io.github.vaan.droid.data.serializers

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.instructions.DataRegistry
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object DynamicDroidDataSerializer : PersistentDataType<PersistentDataContainer, DynamicDroidData> {
    val staticKey = PylonDroid.key("static")

    val registryValuesKey = PylonDroid.key("registry_values")
    val registryValuesType = RebarSerializers.MAP.mapTypeFrom(
        RebarSerializers.ENUM.enumTypeFrom(DataRegistry::class.java),
        ValuedSerializer
    )

    val stackKey = PylonDroid.key("stack")
    val stackType = RebarSerializers.LIST.listTypeFrom(ValuedSerializer)

    val errorKey = PylonDroid.key("error")

    val logKey = PylonDroid.key("log")
    val logType = RebarSerializers.LIST.strings()

    val instructionsKey = PylonDroid.key("instructions")
    val instructionsTypes = RebarSerializers.LIST.listTypeFrom(OperationSerializer)


    override fun getPrimitiveType(): Class<PersistentDataContainer> = PersistentDataContainer::class.java

    override fun getComplexType(): Class<DynamicDroidData> = DynamicDroidData::class.java

    override fun toPrimitive(
        complex: DynamicDroidData,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {
        val pdc = context.newPersistentDataContainer()
        pdc.set(staticKey, StaticDroidDataSerializer, complex.static)

        pdc.set(registryValuesKey, registryValuesType, complex.registryValues)
        pdc.set(stackKey, stackType, complex.stack)
        pdc.set(errorKey, RebarSerializers.BOOLEAN, complex.error)
        pdc.set(logKey, logType, complex.log)
        pdc.set(instructionsKey, instructionsTypes, complex.instructions)

        return pdc
    }

    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): DynamicDroidData {
        val static = primitive.get(staticKey, StaticDroidDataSerializer)!!
        val registryValues = primitive.get(registryValuesKey, registryValuesType)!!
        val stack = primitive.get(stackKey, stackType)!!
        val error = primitive.get(errorKey, RebarSerializers.BOOLEAN)!!
        val log = primitive.get(logKey, logType)!!
        val instructions = primitive.get(instructionsKey, instructionsTypes)!!

        return DynamicDroidData(
            static,
            registryValues,
            stack,
            error,
            log,
            instructions
        )
    }
}