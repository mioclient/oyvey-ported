import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("fabric-loom")
    id("com.github.gmazzo.buildconfig")
    java
}

val mod_version: String by project
val maven_group: String by project
val mod_name: String by project
val archives_base_name: String by project
val minecraft_version: String by project
val loader_version: String by project
val fabric_version: String by project
val catformat_version: String by project

val USING_GIT = Paths.get(rootDir.absolutePath).resolve(".git").toFile().exists()
val GIT_HASH = execute("git rev-parse --short HEAD")

version = mod_version
group = maven_group

buildConfig {
    packageName("$group.util")
    useJavaOutput()
    generateAtSync.set(true)

    val typeString = String::class.java
    val typeBool = Boolean::class.java

    val buildTime = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date())

    buildConfigField(typeString, "NAME", mod_name)
    buildConfigField(typeString, "GROUP", maven_group)
    buildConfigField(typeString, "VERSION", mod_version)
    buildConfigField(typeString, "BUILD_TIME", buildTime)
    buildConfigField(typeBool, "USING_GIT", USING_GIT)
    buildConfigField(typeString, "HASH", GIT_HASH)
    buildConfigField(typeString, "BRANCH", execute("git branch --show-current"))
}

sourceSets.main {
    java.srcDirs("build/gen/buildconfig/src/main")
}

base {
    archivesName.set("$archives_base_name-$GIT_HASH")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

loom {
    accessWidenerPath.set(file("src/main/resources/oyvey.accesswidener"))
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")

    include(implementation("com.github.cattyngmd.catformat:catformat-core:$catformat_version")!!)
    include(modImplementation("com.github.cattyngmd.catformat:catformat-fabric:$catformat_version")!!)
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("minecraft_version", minecraft_version)
    inputs.property("loader_version", loader_version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to version,
            "minecraft_version" to minecraft_version,
            "loader_version" to loader_version
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
    dependsOn("generateBuildConfig")
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.named<Jar>("sourcesJar") {
    dependsOn("generateBuildConfig")
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_$archives_base_name" }
    }
}

fun execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
    isIgnoreExitValue = true
}.standardOutput.asText.getOrElse("nogit").trim().replace("\n", "")
