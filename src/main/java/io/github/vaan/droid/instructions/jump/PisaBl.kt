package io.github.vaan.droid.instructions.jump

import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.instructions.base.PisaBase

object PisaBl : PisaBase("BL", "Branch Link, jumping while saving the return address") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 1) {
            failInstruction(data, "Must have one argument name")
            return
        }

        DataRegistry.LR.set(data, DataRegistry.PC.get(data).clone())
        jump(data, args)
    }

    private fun jump(data: DynamicDroidData, args: Array<String>) {
        val accessor = data.accessorOf(args[0])
        if (accessor == null) {
            failInstruction(data, "Invalid data found at first argument")
            return
        }

        val valued = accessor.get(data)
        val jumpable = valued as? Jumpable ?: (valued as? Valued.CanBeString)?.stringValued()
        if (jumpable == null) {
            failInstruction(data, "Argument is not jumpable")
            return
        }

        val toJump = jumpable.evaluate(data)
        if (toJump == null) {
            failInstruction(data, "Target jump is invalid")
            return
        }

        DataRegistry.PC.set(data, Valued(toJump))
    }
}