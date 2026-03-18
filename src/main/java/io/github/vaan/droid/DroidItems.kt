package io.github.vaan.droid

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material

object DroidItems : InitMeBro {
    val BASE_DROID = ItemStackBuilder.rebar(Material.IRON_BLOCK, DroidKeys.BASE_DROID)
        .build().also {
            RebarItem.register(RebarItem::class.java, it, DroidKeys.BASE_DROID)
            DroidPages.DROIDS.addItem(it)
        }


}