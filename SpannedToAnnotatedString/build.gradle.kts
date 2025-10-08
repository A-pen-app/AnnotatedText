import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.aghajari.compose.text"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}


fun githubProperties() = getPropertiesFromFile("$rootDir/github.properties")

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            // Note that your GitHub username should be in lowercase.
            url = uri("https://maven.pkg.github.com/A-pen-app/AnnotatedText")
            credentials {
                username = (githubProperties()["gpr.usr"] as String?) ?: System.getenv("GITHUB_ACTOR")
                password = (githubProperties()["gpr.key"] as String?) ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("github") {
            groupId = "com.penpeer"
            artifactId = "annotated-text-compose"
            version = "1.0.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

fun getPropertiesFromFile(filePath: String): Properties {
    val propertiesFile = File(filePath)
    val properties = Properties()
    if (propertiesFile.exists()) properties.load(FileInputStream(propertiesFile))
    return properties
}