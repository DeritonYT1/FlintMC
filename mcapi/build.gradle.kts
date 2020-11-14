plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    minecraftVersions("1.15.2")
}

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    v1_15_2AnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-eventbus"))
    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))
    api(project(":framework:framework-tasks"))
    api(project(":transform:transform-hook"))
    api(project(":transform:transform-shadow"))
    api(project(":render:render-model-renderer"))

    api("com.google.code.gson", "gson", "2.8.6")
    internalImplementation(project(":render:render-model-renderer", "internal"))
}
