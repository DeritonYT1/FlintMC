plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":util:util-commons"))
    api(project(":util:util-mapping"))
}
