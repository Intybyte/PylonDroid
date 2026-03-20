package io.github.vaan.droid.instructions.jump

import io.github.vaan.droid.data.DataRegistry
import io.github.vaan.droid.data.DynamicDroidData
import io.github.vaan.droid.data.Valued
import io.github.vaan.droid.instructions.base.PisaBase

abstract class PisaConditionalJmp(name: String, desc: String) : PisaBase(name, desc) {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        if (args.size != 1) {
            failInstruction(data, "Must have one argument name")
            return
        }

        val cmp = DataRegistry.CMP.get(data)
        if (!valid(cmp.value as Int)) {
            return
        }

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

    abstract fun valid(cmp: Int): Boolean
}

object PisaJe : PisaConditionalJmp("JE", "Jump if equal") {
    override fun valid(cmp: Int) = cmp == 0
}

object PisaJne : PisaConditionalJmp("JNE", "Jump if not equal") {
    override fun valid(cmp: Int) = cmp != 0
}

object PisaJlt : PisaConditionalJmp("JLT", "Jump if less than") {
    override fun valid(cmp: Int) = cmp < 0
}

object PisaJgt : PisaConditionalJmp("JGT", "Jump if greater than") {
    override fun valid(cmp: Int) = cmp > 0
}

object PisaJle : PisaConditionalJmp("JLE", "Jump if less or equal") {
    override fun valid(cmp: Int) = cmp <= 0
}

object PisaJge : PisaConditionalJmp("JGE", "Jump if greater or equal") {
    override fun valid(cmp: Int) = cmp >= 0
}

object PisaJmp : PisaConditionalJmp("JMP", "Jumps to offset or label") {
    override fun valid(cmp: Int): Boolean = true
}