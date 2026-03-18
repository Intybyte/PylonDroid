package io.github.vaan.droid.data.serializers

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.Valued
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object ValuedSerializer : PersistentDataType<PersistentDataContainer, Valued<*>> {

    private val typeKey = PylonDroid.key("type")
    private val valueKey = PylonDroid.key("value")

    override fun getPrimitiveType(): Class<PersistentDataContainer> = PersistentDataContainer::class.java

    override fun getComplexType(): Class<Valued<*>> = Valued::class.java

    override fun toPrimitive(
        complex: Valued<*>,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {

        val pdc = context.newPersistentDataContainer()

        when (complex) {
            is Valued.IntVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "int")
                pdc.set(valueKey, RebarSerializers.INTEGER, complex.value)
            }

            is Valued.DoubleVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "double")
                pdc.set(valueKey, RebarSerializers.DOUBLE, complex.value)
            }

            is Valued.LongVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "long")
                pdc.set(valueKey, RebarSerializers.LONG, complex.value)
            }

            is Valued.StringVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "string")
                pdc.set(valueKey, RebarSerializers.STRING, complex.value)
            }

            is Valued.UUIDVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "uuid")
                pdc.set(valueKey, RebarSerializers.UUID, complex.value)
            }

            is Valued.KeyVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "key")
                pdc.set(valueKey, RebarSerializers.NAMESPACED_KEY, complex.value)
            }

            is Valued.EmptyVal -> {
                pdc.set(typeKey, RebarSerializers.STRING, "empty")
            }

            else -> error("Unsupported Valued type: ${complex::class.java.simpleName}")
        }

        return pdc
    }

    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): Valued<*> {

        val type = primitive.get(typeKey, RebarSerializers.STRING)
            ?: error("Missing type in Valued")

        return when (type) {
            "int" -> Valued.IntVal(
                primitive.get(valueKey, RebarSerializers.INTEGER)!!
            )

            "double" -> Valued.DoubleVal(
                primitive.get(valueKey, RebarSerializers.DOUBLE)!!
            )

            "long" -> Valued.LongVal(
                primitive.get(valueKey, RebarSerializers.LONG)!!
            )

            "string" -> Valued.StringVal(
                primitive.get(valueKey, RebarSerializers.STRING)!!
            )

            "uuid" -> Valued.UUIDVal(
                primitive.get(valueKey, RebarSerializers.UUID)!!
            )

            "key" -> Valued.KeyVal(
                primitive.get(valueKey, RebarSerializers.NAMESPACED_KEY)!!
            )

            "empty" -> Valued.EmptyVal

            else -> error("Unknown Valued type: $type")
        }
    }
}