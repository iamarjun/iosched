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

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.samples.apps.iosched.R


private val OpenSans = FontFamily(
    Font(R.font.open_sans_light, FontWeight.W300),
    Font(R.font.open_sans_regular, FontWeight.W400),
    Font(R.font.open_sans_semi_bold, FontWeight.W600),
    Font(R.font.open_sans_bold, FontWeight.W700),
    Font(R.font.open_sans_extra_bold, FontWeight.W800),

//    Font(R.font.open_sans_light_italic, FontWeight.W300),
//    Font(R.font.open_sans_italic, FontWeight.W400),
//    Font(R.font.open_sans_semi_bold_italic, FontWeight.W600),
//    Font(R.font.open_sans_bold_italic, FontWeight.W700),
//    Font(R.font.open_sans_extra_bold_italic, FontWeight.W800),
)


val IOTypography = Typography(
    h1 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 95.sp,
        fontWeight = FontWeight.W300,
    ),
    h2 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 59.sp,
        fontWeight = FontWeight.W300,
    ),
    h3 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 48.sp,
        fontWeight = FontWeight.W400
    ),
    h4 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 34.sp,
        fontWeight = FontWeight.W400,
    ),
    h5 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 24.sp,
        fontWeight = FontWeight.W400
    ),
    h6 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 20.sp,
        fontWeight = FontWeight.W500,
    ),
    subtitle1 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 16.sp,
        fontWeight = FontWeight.W400,
    ),
    subtitle2 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
    ),
    body1 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 16.sp,
        fontWeight = FontWeight.W400,
    ),
    body2 = TextStyle(
        fontFamily = OpenSans,
        fontSize = 14.sp,
        fontWeight = FontWeight.W400,
    ),
    button = TextStyle(
        fontFamily = OpenSans,
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
    ),
    caption = TextStyle(
        fontFamily = OpenSans,
        fontSize = 12.sp,
        fontWeight = FontWeight.W400,
    ),
    overline = TextStyle(
        fontFamily = OpenSans,
        fontSize = 10.sp,
        fontWeight = FontWeight.W400,
    )
)