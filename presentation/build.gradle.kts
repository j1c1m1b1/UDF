plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.kotlin)
}

dependencies {
    api(Libraries.kotlinStdLib)
    api(Libraries.kotlinCoroutines)
}