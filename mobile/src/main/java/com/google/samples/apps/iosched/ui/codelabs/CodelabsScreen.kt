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

package com.google.samples.apps.iosched.ui.codelabs

import android.content.Context
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.samples.apps.iosched.R
import com.google.samples.apps.iosched.model.Codelab
import com.google.samples.apps.iosched.model.Tag
import com.google.samples.apps.iosched.ui.MainActivityViewModel
import com.google.samples.apps.iosched.ui.theme.Black
import com.google.samples.apps.iosched.ui.theme.IOTheme
import com.google.samples.apps.iosched.ui.theme.White
import com.google.samples.apps.iosched.util.openWebsiteUri

@ExperimentalAnimationApi
@Composable
fun CodelabsScreen(
    mainViewModel: MainActivityViewModel,
    viewModel: CodelabsViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    //TODO: Once the full migration is done, move this to the top level
    IOTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text("Codelabs", style = MaterialTheme.typography.h4) },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launch),
                                contentDescription = "Launch Codelabs"
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = rememberCoilPainter(
                                    request = "https://avatars.githubusercontent.com/u/19618555?v=4",
                                ),
                                contentDescription = "Launch Codelabs"
                            )
                        }
                    },
                    backgroundColor = White,
                    contentColor = Black,
                    elevation = 8.dp
                )
            }
        ) {

            val modifier = Modifier.padding(it)

            val screenState by viewModel.screenContent.collectAsState()

            val context = LocalContext.current
            Codelabs(
                modifier = modifier,
                screenState = screenState,
                dismissCodelabsInfoCard = { viewModel.dismissCodelabsInfoCard() },
                onCodelabClick = {
                    startCodelab(context, it)
                }
            )

        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun Codelabs(
    modifier: Modifier,
    screenState: CodelabsScreenState,
    dismissCodelabsInfoCard: () -> Unit,
    onCodelabClick: (Codelab) -> Unit,
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        item {
            AnimatedVisibility(visible = screenState.infoCardDismissed.not()) {
                CodelabsInformationCard(
                    modifier = modifier,
                    dismissCodelabsInfoCard = dismissCodelabsInfoCard
                )
            }
        }

        items(screenState.codelabsList) { codelab ->
            CodeLab(
                modifier = modifier,
                codelab = codelab,
                onCodelabClick = {
                    onCodelabClick(codelab)
                }
            )
        }
    }

}

@ExperimentalAnimationApi
private fun startCodelab(context: Context, codelab: Codelab) {
    if (codelab.hasUrl()) {
        openWebsiteUri(context, addCodelabsAnalyticsQueryParams(codelab.codelabUrl))
//        analyticsHelper.logUiEvent("Start codelab \"${codelab.title}\"", AnalyticsActions.CLICK)
    }
}

@ExperimentalAnimationApi
private fun addCodelabsAnalyticsQueryParams(url: String): Uri {
    return Uri.parse(url)
        .buildUpon()
        .appendQueryParameter(CodelabsFragment.PARAM_UTM_SOURCE, CodelabsFragment.VALUE_UTM_SOURCE)
        .appendQueryParameter(CodelabsFragment.PARAM_UTM_MEDIUM, CodelabsFragment.VALUE_UTM_MEDIUM)
        .build()
}

@Composable
private fun CodelabsInformationCard(
    modifier: Modifier,
    dismissCodelabsInfoCard: () -> Unit,
) {
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(
            modifier = modifier,
        ) {
            Text(text = stringResource(id = R.string.codelabs_information))
            Button(
                onClick = { dismissCodelabsInfoCard() },
                modifier = modifier
            ) {
                Text(text = stringResource(id = R.string.got_it))
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun CodeLab(
    modifier: Modifier,
    codelab: Codelab,
    onCodelabClick: () -> Unit,
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val rotation: Float by animateFloatAsState(
        targetValue = if (isExpanded) -180f else 0f, animationSpec = tween(
            durationMillis = 250,
            delayMillis = 0,
            easing = LinearEasing
        )
    )

    Surface(modifier = modifier
        .clickable {
            isExpanded = !isExpanded
        }) {
        Row(
            modifier = modifier.padding(6.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .size(32.dp),
                painter = rememberCoilPainter(request = codelab.iconUrl),
                contentDescription = "Codelab Icon"
            )
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = codelab.title,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.W600)
                )
                Text(
                    text = "Duration: ${codelab.durationMinutes} min",
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.W400,
                        color = Color.Gray
                    )
                )
                Row {
                    codelab.tags.forEach {
                        Tag(
                            modifier = modifier,
                            tag = it
                        )
                    }
                }
                AnimatedVisibility(visible = isExpanded) {
                    CodelabInfo(
                        modifier = modifier,
                        codelab = codelab
                    ) {
                        onCodelabClick()
                    }
                }

            }

            IconButton(onClick = {
                isExpanded = !isExpanded
            }) {
                Icon(
                    modifier = modifier.rotate(rotation),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand"
                )
            }
        }
    }
}

@Composable
private fun CodelabInfo(
    modifier: Modifier,
    codelab: Codelab,
    onCodelabClick: () -> Unit,
) {

    Column(modifier = modifier) {
        Spacer(modifier = modifier.height(12.dp))
        Text(
            text = codelab.description,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.W400,
                color = Color.Gray
            )
        )
        Spacer(modifier = modifier.height(12.dp))
        Surface(modifier = modifier.clickable {
            onCodelabClick()
        }) {
            Row(
                modifier = modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launch),
                    contentDescription = "Launch Codelabs",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = modifier.width(5.dp))
                Text(
                    text = "Start codelab",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.W600
                    )
                )
            }
        }
    }
}

@Composable
private fun Tag(
    modifier: Modifier,
    tag: Tag
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .wrapContentSize(Alignment.Center)
                .clip(CircleShape)
                .size(10.dp)
                .background(Color(tag.color))
        )
        Text(
            text = tag.displayName,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.W400,
                color = Color.Gray
            )
        )
        Spacer(modifier = modifier.width(10.dp))
    }
}

data class CodelabsScreenState(
    val loading: Boolean = false,
    val infoCardDismissed: Boolean = false,
    val codelabsList: List<Codelab> = emptyList()
)

