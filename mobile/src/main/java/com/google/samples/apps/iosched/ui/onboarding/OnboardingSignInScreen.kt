/*
 * Copyright 2019 Google LLC
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

package com.google.samples.apps.iosched.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.samples.apps.iosched.R

@Composable
fun OnboardingSignInScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {

    Scaffold(scaffoldState = scaffoldState) {
        val modifier = Modifier.padding(it)
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextButton(
                modifier = modifier.align(Alignment.End),
                onClick = { }
            ) {
                Text(text = "Sign In")
            }

            Image(
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.onboarding_schedule),
                contentDescription = "null"
            )

            Text(
                text = stringResource(id = R.string.onboarding_signin),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(50.dp))
        }
    }

}