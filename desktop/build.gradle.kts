import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.2.10"
    id("org.jetbrains.compose") version "1.8.2"
}

group = "com.example.bluey"
version = "1.0.0"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
}

compose.desktop {
    application {
        mainClass = "com.example.bluey.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "Bluey"
            packageVersion = "1.0.0"
            description = "Bluey blueberry focus companion"
            vendor = "Bluey"

            windows {
                console = false
                dirChooser = true
                perUserInstall = true
                menuGroup = "Bluey"
                shortcut = true
            }
        }
    }
}
