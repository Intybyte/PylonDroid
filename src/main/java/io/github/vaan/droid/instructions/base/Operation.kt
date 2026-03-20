package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DynamicDroidData

data class Operation(
    val instruction: Instruction,
    val args: Array<String>
) {
    fun execute(data: DynamicDroidData) {
        instruction.execute(data, args)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Operation

        if (instruction != other.instruction) return false
        if (!args.contentEquals(other.args)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = instruction.hashCode()
        result = 31 * result + args.contentHashCode()
        return result
    }

    override fun toString(): String {
        val name = instruction.name.uppercase()

        return if (args.isEmpty()) {
            name
        } else {
            "$name ${args.joinToString(", ")}"
        }
    }

    companion object {
        fun of(str: String): Operation? {
            // "ADD A, 1, 2"
            val parts = str.trim().split(Regex("\\s+"), limit = 2)

            val opcode = parts[0].lowercase()
            val instruction = if (opcode != ";") {
                try {
                    val key = PylonDroid.key(opcode)
                    Instruction.REGISTRY[key] ?: return null
                } catch (_: Exception) {
                    return null
                }
            } else {
                PisaComment
            }

            val args = parts.getOrNull(1)
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.toTypedArray()
                ?: emptyArray()

            return Operation(instruction, args)
        }
    }
}
