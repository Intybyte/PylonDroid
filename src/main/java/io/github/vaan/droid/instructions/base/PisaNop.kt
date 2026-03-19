package io.github.vaan.droid.instructions.base

import io.github.vaan.droid.data.DynamicDroidData

object PisaNop : PisaBase("NOP", "Does nothing") {
    override fun execute(data: DynamicDroidData, args: Array<String>) {
        
    }
}