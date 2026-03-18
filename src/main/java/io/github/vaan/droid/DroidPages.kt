package io.github.vaan.droid

import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import org.bukkit.Material

object DroidPages {
    val DROIDS = SimpleStaticGuidePage(PylonDroid.key("droid_page"))

    fun init() {
        RebarGuide.rootPage.addPage(Material.EMERALD, DROIDS)
    }
}