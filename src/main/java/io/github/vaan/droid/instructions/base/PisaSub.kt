package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.data.DynamicDroidData

object PisaSub : PisaBase("Sub", "Subtracts two numbers") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 3) {
            failInstruction(data, "Must have 3 arguments")
            return
        }

        val accessors = args.map(data::accessorOf)
        if (accessors.any { it == null }) {
            failInstruction(data, "Invalid data storage found")
            return
        }

        val arg1 = accessors[1]!!.get(data)?.value
        val arg2 = accessors[2]!!.get(data)?.value

        if (arg1 == null || arg2 == null) {
            failInstruction(data, "Data in one of the two registries required is null")
            return
        }

        if (arg1 !is Number || arg2 !is Number) {
            failInstruction(data, "The last two arguments must be or store a number, instead found $arg1 & $arg2")
            return
        }

        val result = when {
            arg1 is Double || arg2 is Double -> arg1.toDouble() - arg2.toDouble()
            arg1 is Float  || arg2 is Float  -> arg1.toFloat() - arg2.toFloat()
            arg1 is Long   || arg2 is Long   -> arg1.toLong() - arg2.toLong()
            else -> arg1.toInt() - arg2.toInt()
        }

        accessors[0]!!.set(data, result)
    }
}