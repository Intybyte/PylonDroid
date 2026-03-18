package io.github.vaan.droid

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.vaan.droid.content.DroidBlock
import org.bukkit.Material

object DroidBlocks : InitMeBro {

    override fun init() {
        RebarBlock.register(DroidKeys.BASE_DROID, Material.IRON_BLOCK, DroidBlock::class.java)
    }
}