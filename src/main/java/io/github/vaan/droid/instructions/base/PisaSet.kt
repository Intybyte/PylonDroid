package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.data.DynamicDroidData

object PisaSet : PisaBase("SET", "Set a registry/stack value") {
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

        accessors[0]!!.set(data, accessors[1]!!.get(data))
    }
}