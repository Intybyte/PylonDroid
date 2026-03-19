package io.github.vaan.droid

import io.github.vaan.droid.instructions.base.PisaNop
import io.github.vaan.droid.instructions.base.arithmetic.PisaAdd
import io.github.vaan.droid.instructions.base.arithmetic.PisaDiv
import io.github.vaan.droid.instructions.base.arithmetic.PisaMul
import io.github.vaan.droid.instructions.base.PisaPrint
import io.github.vaan.droid.instructions.base.PisaSet
import io.github.vaan.droid.instructions.base.arithmetic.PisaSub
import io.github.vaan.droid.instructions.base.jump.PisaJmp
import io.github.vaan.droid.instructions.base.jump.PisaLabel

object DroidInstructions {
    fun init() {
        // arithmetic
        PisaAdd.init()
        PisaSub.init()
        PisaMul.init()
        PisaDiv.init()

        PisaSet.init()
        PisaPrint.init()
        PisaNop.init()

        // jmp instructions
        PisaLabel.init()
        PisaJmp.init()
    }
}