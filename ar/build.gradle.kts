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

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    buildTypes {
        maybeCreate("staging")
        getByName("staging") {

            // Specifies a sorted list of fallback build types that the
            // plugin should try to use when a dependency does not include a
            // "staging" build type.
            // Used with :test-shared, which doesn't have a staging variant.
            matchingFallbacks.add("debug")
        }
    }

    lint {
        disable("InvalidPackage")
        // Version changes are beyond our control, so don't warn. The IDE will still mark these.
        disable("GradleDependency")
    }

    // Required by ArWebView
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.ar:core:1.24.0")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0")
}
