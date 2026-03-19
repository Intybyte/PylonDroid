package io.github.vaan.droid

import io.github.vaan.droid.instructions.base.PisaAdd
import io.github.vaan.droid.instructions.base.PisaDiv
import io.github.vaan.droid.instructions.base.PisaMul
import io.github.vaan.droid.instructions.base.PisaPrint
import io.github.vaan.droid.instructions.base.PisaSet
import io.github.vaan.droid.instructions.base.PisaSub

object DroidInstructions {
    fun init() {
        // math
        PisaAdd.init()
        PisaSub.init()
        PisaMul.init()
        PisaDiv.init()

        PisaSet.init()
        PisaPrint.init()
    }
}