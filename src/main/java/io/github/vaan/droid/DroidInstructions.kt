package io.github.vaan.droid

import io.github.vaan.droid.instructions.base.PisaAdd
import io.github.vaan.droid.instructions.base.PisaPrint
import io.github.vaan.droid.instructions.base.PisaSet

object DroidInstructions {
    fun init() {
        PisaAdd.init()
        PisaSet.init()
        PisaPrint.init()
    }
}