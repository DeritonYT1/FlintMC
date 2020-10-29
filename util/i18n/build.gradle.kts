plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    "internalAnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))
    "v1_15_2AnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-stereotype"))
    api(project(":transform:transform-minecraft"))
}
