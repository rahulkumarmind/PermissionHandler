plugins {
    id("maven-publish")
    id("com.android.library")
}

android {
    namespace = "com.rahul.permissionhelper"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {

                from(components.findByName("release"))

                groupId = "com.github.rahulkumarmind"
                artifactId = "PermissionHelper"
                version = "1.0.0"
            }
        }
    }
}