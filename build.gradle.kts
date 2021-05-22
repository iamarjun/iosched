/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Top-level build file where you can add configuration options common to all
// sub-projects/modules.
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    repositories {
        google()
        mavenCentral()
        jcenter()
        // Android Build Server
        maven { url = uri("../iosched-prebuilts/m2repository") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-beta02")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("com.google.gms:google-services:4.3.8")
        classpath("androidx.benchmark:benchmark-gradle-plugin:1.0.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.6.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.35.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()

        // For Android Build Server
        // - Material Design Components
        maven { url = uri("${project.rootDir}/../iosched-prebuilts/repository") }
        // - Other dependencies
        maven { url = uri("${project.rootDir}/../iosched-prebuilts/m2repository") }
        // - Support Libraries, etc
        maven {
            url =
                uri("${project.rootDir}/../../../prebuilts/fullsdk/linux/extras/support/m2repository")
        }

        flatDir {
            dirs = setOf(file("libs"), project(":ar").file("libs"))
        }
    }
}
