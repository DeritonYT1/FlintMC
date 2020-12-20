plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
}

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-eventbus"))
    api(project(":framework:framework-stereotype"))
    api(project(":mcapi"))
    api(project(":transform:transform-hook"))
}