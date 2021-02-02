const val kotlinVersion = "1.4.21"

object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "4.1.2"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val javaLibrary= "java-library"
    const val kotlin = "kotlin"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
}

object AndroidSdk {
    const val min = 21
    const val compile = 30
    const val target = compile
}

object Modules {
    const val presentation = ":presentation"
}

object Libraries {
    private object Versions {
        const val appCompat = "1.2.0"
        const val material = "1.2.1"
        const val ktx = "1.3.2"
        const val coroutinesVersion = "1.4.2"
        const val lifecycleKtxVersion = "2.2.0"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleKtxVersion}"
}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.12"
        const val junitExt = "1.1.2"
        const val espresso = "3.3.0"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}