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
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        create("staging") {
        }
    }

    lint {
        // Version changes are beyond our control, so don't warn. The IDE will still mark these.
        disable("GradleDependency")
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0")

    // Architecture Components
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha01")
}
