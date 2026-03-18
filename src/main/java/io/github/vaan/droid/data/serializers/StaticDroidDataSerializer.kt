package io.github.vaan.droid.data.serializers

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.StaticDroidData
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object StaticDroidDataSerializer : PersistentDataType<PersistentDataContainer, StaticDroidData> {
    val type = PylonDroid.key("type")

    override fun getPrimitiveType(): Class<PersistentDataContainer> = PersistentDataContainer::class.java

    override fun getComplexType(): Class<StaticDroidData> = StaticDroidData::class.java

    override fun toPrimitive(complex: StaticDroidData, context: PersistentDataAdapterContext): PersistentDataContainer {
        val pdc = context.newPersistentDataContainer()
        pdc.set(type, RebarSerializers.NAMESPACED_KEY, complex.key)
        return pdc
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): StaticDroidData {
        val key = primitive.get(type, RebarSerializers.NAMESPACED_KEY)!!
        return StaticDroidData(key)
    }
}