import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

// Specify what plugins to use
plugins {
    kotlin("jvm") version "2.2.0"
    // IntelliJ plugin
    idea
    // used to package needed dependencies into the jar
    id("com.gradleup.shadow") version "9.2.2"
    // used to generate plugin.yml
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    // used to run a test server locally
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

// Specify the 'group' (eg: io.github.pylonmc.myaddon)
group = project.properties["group"]!!

// Add repositories from which to download dependencies
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://jitpack.io") {
        name = "JitPack"
    }
    maven("https://repo.xenondevs.xyz/releases")
}

// Get dependency versions from gradle.properties
val coreVersion = project.properties["pylon-core.version"] as String
val rebarPosition = "/IdeaProjects/parallel-dev-repo/rebar/rebar/build/libs/rebar-1.0.0-SNAPSHOT.jar"

// Download dependencies
dependencies {
    library(kotlin("stdlib"))
    compileOnly(files(rebarPosition))
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("io.github.pylonmc:rebar:$coreVersion")
}

// Settings for IntelliJ
idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

kotlin {
    // Target JVM 21
    jvmToolchain(21)
}

// Configuration for the output JAR
tasks.shadowJar {
    archiveClassifier = ""
}

// Generate the plugin.yml file using the bukkit gradle plugin
bukkit {
    name = project.name
    main = project.properties["main-class"] as String
    version = project.version.toString()
    apiVersion = "1.21"
    depend = listOf("PylonCore", "PylonBase")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}

// Run a server using the run server gradle plugin
tasks.runServer {
    doFirst {
        // Remove the plugins folder. This is so any changes to language files etc are propagated.
        val runFolder = project.projectDir.resolve("run")
        val pluginsDir = runFolder.resolve("plugins")
        if (!System.getProperty("io.github.pylonmc.pylon.disableConfigReset").toBoolean()) {
            pluginsDir.deleteRecursively()
        }

        pluginsDir.mkdirs()
        copy {
            include(rebarPosition)
            into(pluginsDir)
        }
    }

    maxHeapSize = "8G"
    minecraftVersion("1.21.8")
}