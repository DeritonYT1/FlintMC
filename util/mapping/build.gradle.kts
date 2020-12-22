plugins {
    id("java-library")
}

group = "net.flintmc"



dependencies {
    minecraft("1.15.2") {
        annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    }
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":launcher"))
    api(project(":framework:framework-inject"))
    api(project(":util:util-csv"))

    api("org.ow2.asm", "asm", "7.2-beta")
    api("org.javassist", "javassist", "3.27.0-GA")
    api("com.google.code.gson", "gson", "2.8.6")

}
