plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.kotlinAndroid)
}

dependencies {
    api(Libraries.kotlinStdLib)
    api(Libraries.kotlinCoroutines)
}