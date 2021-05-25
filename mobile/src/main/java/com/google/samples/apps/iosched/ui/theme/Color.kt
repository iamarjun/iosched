/*
 * Copyright 2021 Google LLC
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

package com.google.samples.apps.iosched.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val DeepSkyBlue = Color(0xff1a73e8)
val WarmBlue = Color(0xff574ddd)

val CarolineBlue = Color(0xff8ab4f8)
val CornFlowerBlue = Color(0xff669DF6)

val Teal = Color(0xff069f86)
val BrightLightBlue = Color(0xff27e5fd)
val SunYellow = Color(0xfffcd230)
val BrightOrange = Color(0xffff6c00)
val White = Color.White
val Black = Color.Black
val Transparent = Color.Transparent

val Red800 = Color(0xFFB10000)
val Red200 = Color(0xFFEF9A9A)


val IOLightThemeColors = lightColors(
    primary = DeepSkyBlue,
    primaryVariant = WarmBlue,
    onPrimary = White,
    secondary = DeepSkyBlue,
    secondaryVariant = WarmBlue,
    onSecondary = Color.White,
    error = Red800
)

val IODarkThemeColors = darkColors(
    primary = CarolineBlue,
    primaryVariant = CornFlowerBlue,
    onPrimary = Black,
    secondary = CarolineBlue,
    onSecondary = Black,
    error = Red200
)