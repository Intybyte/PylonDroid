package io.github.vaan.droid.instructions.jump

import io.github.vaan.droid.data.DynamicDroidData

interface Jumpable {
    fun evaluate(data: DynamicDroidData): Int?
}