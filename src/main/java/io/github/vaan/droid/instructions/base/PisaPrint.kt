package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.data.DynamicDroidData
import org.bukkit.Bukkit

object PisaPrint : PisaBase("PRINT", "Prints something") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 1) {
            failInstruction(data, "Must have 1 argument")
            return
        }

        val accessor = data.accessorOf(args[0])
        if (accessor == null) {
            failInstruction(data, "Invalid data storage found")
            return
        }

        data.addLog("PRINT", "${accessor.get(data).value?.toString()}")
    }
}