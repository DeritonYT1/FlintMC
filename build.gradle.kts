plugins {
    id("net.flintmc.flint-gradle")
}

fun RepositoryHandler.flintRepository() {
    maven {
        setUrl("https://dist.labymod.net/api/v1/maven/release")
        name = "Flint"
        credentials(HttpHeaderCredentials::class) {
            name = "Authorization"
            value = "Bearer CbtTjzAOuDBr5QXcGnBc1MB3eIHxcZetnyHtdN76VpTNgbwAf87bzWPCntsXwj52"
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}

repositories {
    mavenLocal()
    flintRepository()
    mavenCentral()
}

val allProjectsJava: MutableList<SourceDirectorySet> = ArrayList()
val allProjectsClasspath: MutableList<FileCollection> = ArrayList()

tasks.create<Javadoc>("generateAllDocs") {
    setDestinationDir(file("docs"))
}

subprojects {

    plugins.withId("java") {
        apply<MavenPublishPlugin>()

        version = System.getenv().getOrDefault("VERSION", "1.0.0")

        repositories {
            flintRepository()
            mavenCentral()
        }

        tasks.withType<JavaCompile> {
            options.isFork = true
        }

        allProjectsJava.add(sourceSets.getByName("main").allJava)
        allProjectsClasspath.add(sourceSets.getByName("main").compileClasspath)
    }
}

tasks.create<Javadoc>("allDocs") {
    setDestinationDir(file("generated-docs"))
    setSource(allProjectsJava)
    classpath = files(allProjectsClasspath)
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.apache.logging.log4j:log4j-api:2.8.2")
            force("com.google.guava:guava:27.0.1-jre")
            force("org.apache.commons:commons-lang3:3.10")
            force("org.apache.logging.log4j:log4j-core:2.8.2")
            force("it.unimi.dsi:fastutil:8.2.1")
            force("net.java.dev.jna:jna:4.4.0")
            force("com.google.code.findbugs:jsr305:3.0.2")
            force("com.google.code.gson:gson:2.8.6")
            force("commons-io:commons-io:2.6")
            force("commons-codec:commons-codec:1.10")
            force("com.beust:jcommander:1.78")
        }
    }
}

flint {
    flintVersion = System.getenv().getOrDefault("VERSION", "1.0.0")

    projectFilter {
        !arrayOf(":", ":framework", ":render", ":transform", ":util", ":minecraft").contains(it.path)
    }

    minecraftVersions("1.15.2")

    type = net.flintmc.gradle.extension.FlintGradleExtension.Type.LIBRARY
    authors = arrayOf("LabyMedia GmbH")

    runs {
        overrideMainClass("net.flintmc.launcher.FlintLauncher")
    }
}
