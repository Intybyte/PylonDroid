package io.github.vaan.droid

import io.github.vaan.droid.instructions.base.PisaNop
import io.github.vaan.droid.instructions.base.PisaPrint
import io.github.vaan.droid.instructions.base.PisaSet
import io.github.vaan.droid.instructions.base.arithmetic.*
import io.github.vaan.droid.instructions.base.jump.*

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
        PisaBl.init()
        PisaRet.init()
        PisaJmp.init()

        // jmp conditionals
        PisaCmp.init()
        PisaJmp.init()
        PisaJe.init()
        PisaJne.init()
        PisaJlt.init()
        PisaJgt.init()
        PisaJle.init()
        PisaJge.init()
    }
}