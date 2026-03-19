package io.github.vaan.droid.instructions.base.jump

import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.instructions.base.PisaBase

object PisaCmp : PisaBase("Sub", "Subtracts two numbers") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 2) {
            failInstruction(data, "Must have 2 arguments")
            return
        }

        val accessors = args.map(data::accessorOf)
        if (accessors.any { it == null }) {
            failInstruction(data, "Invalid data storage found")
            return
        }

        val arg1 = accessors[0]!!.get(data)?.value
        val arg2 = accessors[1]!!.get(data)?.value

        if (arg1 == null || arg2 == null) {
            failInstruction(data, "Data in one of the two registries required is null")
            return
        }

        if (arg1 !is Number || arg2 !is Number) {
            failInstruction(data, "The last two arguments must be or store a number, instead found $arg1 & $arg2")
            return
        }

        val cmp: Int
        if (arg1 is Double || arg2 is Double || arg1 is Float || arg2 is Float) {
            val a = arg1.toDouble()
            val b = arg2.toDouble()

            cmp = if (a < b) {
                -1
            } else if (a == b) {
                0
            } else {
                1
            }
        } else if (arg1 is Long || arg2 is Long) {
            val a = arg1.toLong()
            val b = arg2.toLong()

            cmp = if (a < b) {
                -1
            } else if (a == b) {
                0
            } else {
                1
            }
        } else {
            val a = arg1.toInt()
            val b = arg2.toInt()

            cmp = if (a < b) {
                -1
            } else if (a == b) {
                0
            } else {
                1
            }
        }

        DataRegistry.CMP.set(data, Valued.IntVal(cmp))
    }
}