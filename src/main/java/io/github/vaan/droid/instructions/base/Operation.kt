package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.PylonDroid
import io.github.vaan.droid.data.DynamicDroidData

data class Operation(
    val instruction: Instruction,
    val args: Array<String>,
    val comment: String? = null
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
        val strBuilder = StringBuilder()
            .append(instruction.name.uppercase() + " ")

        if (args.isNotEmpty()) {
            strBuilder.append(args.joinToString(", "))
        }

        if (comment != null) {
            strBuilder.append(" ; $comment")
        }

        return strBuilder.toString()
    }

    fun operationString(): String {
        val strBuilder = StringBuilder()
            .append(instruction.name.uppercase() + " ")

        if (args.isNotEmpty()) {
            strBuilder.append(args.joinToString(", "))
        }

        return strBuilder.toString()
    }

    companion object {
        fun of(str: String): Operation? {
            // "ADD A, 1, 2"
            val parts = str.trim().split(Regex("\\s+"), limit = 2)

            // get opcode comment if it is present here
            val splittedOpcode = parts[0].split(';', limit = 2)
            val opcode = splittedOpcode[0].lowercase()

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

            if (args.isNotEmpty()) {

                val last = args[args.size - 1]
                val splitComment = last.split(';', limit = 2)

                // no comment
                if (splitComment.size == 1) {
                    return Operation(instruction, args)
                } else {
                    // comment found
                    args[args.size - 1] = splitComment[0]
                    return Operation(instruction, args, splitComment[1].trim())
                }
            } else if (splittedOpcode.size == 2) {
                // comment at opcode
                return Operation(instruction, args, splittedOpcode[1].trim())
            } else {
                return Operation(instruction, args, null)
            }
        }
    }
}
