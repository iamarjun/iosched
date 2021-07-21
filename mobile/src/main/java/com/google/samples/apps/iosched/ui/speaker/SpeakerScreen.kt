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

package com.google.samples.apps.iosched.ui.speaker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.samples.apps.iosched.R
import com.google.samples.apps.iosched.model.Speaker
import com.google.samples.apps.iosched.model.SpeakerId
import com.google.samples.apps.iosched.model.userdata.UserSession
import com.google.samples.apps.iosched.ui.SectionHeader
import com.google.samples.apps.iosched.ui.schedule.ScheduleTwoPaneViewModel
import com.google.samples.apps.iosched.ui.schedule.sessionDateTimeLocation
import com.google.samples.apps.iosched.ui.theme.IOTheme
import com.google.samples.apps.iosched.util.openWebsiteUrl
import com.google.samples.apps.iosched.widget.Loader
import java.time.ZoneId

@ExperimentalComposeUiApi
@Composable
fun SpeakerScreen(
    speakerId: SpeakerId,
    onBackPress: () -> Unit,
    speakerViewModel: SpeakerViewModel = hiltViewModel(),
    scheduleTwoPaneViewModel: ScheduleTwoPaneViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    speakerViewModel.setSpeakerId(speakerId = speakerId)
    val state by speakerViewModel.speakerScreenState.collectAsState()
    val context = LocalContext.current

    IOTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                ) {
                    IconButton(
                        modifier = Modifier.align(Alignment.TopStart),
                        onClick = {
                            onBackPress()
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Navigation",
                            tint = Color.Gray
                        )
                    }

                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(130.dp)
                            .clip(CircleShape),
                        painter = rememberCoilPainter(request = state.speaker?.imageUrl),
                        contentDescription = "Speaker's Photo"
                    )

                }
            },
        ) {

            val modifier = Modifier.padding(it)

            if (state.loading)
                Loader(modifier = modifier)
            else
                SpeakerCard(
                    modifier = modifier,
                    speaker = state.speaker,
                    zoneId = state.zoneId,
                    speakerSessions = state.speakerSession,
                    openWebsite = { openWebsiteUrl(context, state.speaker?.websiteUrl) },
                    openTwitter = { openWebsiteUrl(context, state.speaker?.twitterUrl) },
                    openGithub = { openWebsiteUrl(context, state.speaker?.githubUrl) },
                    openLinkedIn = { openWebsiteUrl(context, state.speaker?.linkedInUrl) }
                )

        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SpeakerCard(
    modifier: Modifier,
    speaker: Speaker?,
    zoneId: ZoneId,
    speakerSessions: List<UserSession>,
    openWebsite: () -> Unit,
    openTwitter: () -> Unit,
    openGithub: () -> Unit,
    openLinkedIn: () -> Unit,
) {

    LazyColumn(modifier = modifier) {

        val list = buildMergedList(speakerSessions)

        items(list) { item ->
            when (item) {
                SpeakerItem -> SpeakerInfoCard(
                    speaker = speaker,
                    openWebsite = openWebsite,
                    openTwitter = openTwitter,
                    openGithub = openGithub,
                    openLinkedIn = openLinkedIn
                )
                is UserSession -> ItemSessionCard(zoneId = zoneId, userSession = item)
                is SectionHeader -> HeaderCard(sectionHeader = item)
                else -> throw IllegalStateException("Unknown item $item")
            }
        }
    }
}

@Composable
fun SpeakerInfoCard(
    speaker: Speaker?,
    openWebsite: () -> Unit,
    openTwitter: () -> Unit,
    openGithub: () -> Unit,
    openLinkedIn: () -> Unit,
) {
    speaker?.let {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = it.name,
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { openWebsite() }) {
                    Text(text = "Website")
                }
                Icon(
                    painter = painterResource(id = R.drawable.divider_slash),
                    contentDescription = null
                )
                TextButton(onClick = { openTwitter() }) {
                    Text(text = "Twitter")
                }
                Icon(
                    painter = painterResource(id = R.drawable.divider_slash),
                    contentDescription = null
                )
                TextButton(onClick = { openGithub() }) {
                    Text(text = "Github")
                }
                Icon(
                    painter = painterResource(id = R.drawable.divider_slash),
                    contentDescription = null
                )
                TextButton(onClick = { openLinkedIn() }) {
                    Text(text = "Linkedin")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = it.biography)
        }
    }
}

@Composable
private fun HeaderCard(
    sectionHeader: SectionHeader
) {
    Text(
        modifier = Modifier.padding(16.dp),
        text = stringResource(id = sectionHeader.titleId),
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
    )
}

@ExperimentalComposeUiApi
@Composable
private fun ItemSessionCard(
    zoneId: ZoneId,
    userSession: UserSession,
//    onStarClick: (UserSession) -> Unit,
//    openEventDetail: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
//                openEventDetail(userSession.session.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = userSession.session.title,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_livestreamed),
                    contentDescription = null,
                    tint = Color.Gray,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = sessionDateTimeLocation(
                        userSession.session.startTime,
                        zoneId,
                        false,
                        userSession.session.room
                    ),
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.Gray
                    )
                )
            }
        }
        IconButton(
            onClick = {
//                onStarClick(userSession)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_border),
                contentDescription = null,
                tint = Color.Gray,
            )
        }
    }

}

private fun buildMergedList(
    sessions: List<UserSession>
): List<Any> {
    val merged = mutableListOf<Any>(SpeakerItem)
    if (sessions.isNotEmpty()) {
        merged += SectionHeader(R.string.speaker_events_subhead)
        merged.addAll(sessions)
    }
    return merged
}
