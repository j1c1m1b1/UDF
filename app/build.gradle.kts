plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
}

android {
    compileSdkVersion(AndroidSdk.compile)
    defaultConfig {
        applicationId = "com.j1c1m1b1.udf"
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.appCompat)
    implementation(Libraries.ktxCore)
    implementation(Libraries.material)

    testImplementation (TestLibraries.junit4)
    androidTestImplementation (TestLibraries.junitExt)
    androidTestImplementation (TestLibraries.espresso)
}