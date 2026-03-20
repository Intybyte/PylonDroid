package io.github.vaan.droid

import io.github.vaan.droid.instructions.base.PisaComment
import io.github.vaan.droid.instructions.base.PisaDie
import io.github.vaan.droid.instructions.base.PisaNop
import io.github.vaan.droid.instructions.base.PisaPrint
import io.github.vaan.droid.instructions.base.PisaSet
import io.github.vaan.droid.instructions.arithmetic.*
import io.github.vaan.droid.instructions.jump.*
import io.github.vaan.droid.instructions.stack.PisaPop
import io.github.vaan.droid.instructions.stack.PisaPush

object DroidInstructions {
    fun init() {
        // simple
        PisaSet.init()
        PisaPrint.init()
        PisaNop.init()
        PisaDie.init()
        PisaComment.init()

        // arithmetic
        PisaAdd.init()
        PisaSub.init()
        PisaMul.init()
        PisaDiv.init()

        // jmp instructions
        PisaLabel.init()
        PisaBl.init()
        PisaRet.init()
        PisaJmp.init()

        // jmp conditionals
        PisaCmp.init()
        PisaJe.init()
        PisaJne.init()
        PisaJlt.init()
        PisaJgt.init()
        PisaJle.init()
        PisaJge.init()

        // stack stuff
        PisaPop.init()
        PisaPush.init()
    }
}