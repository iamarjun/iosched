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

package com.google.samples.apps.iosched.ui.sessiondetail

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.samples.apps.iosched.R
import com.google.samples.apps.iosched.model.*
import com.google.samples.apps.iosched.model.userdata.UserSession
import com.google.samples.apps.iosched.shared.domain.users.SwapRequestParameters
import com.google.samples.apps.iosched.shared.util.toEpochMilli
import com.google.samples.apps.iosched.ui.SectionHeader
import com.google.samples.apps.iosched.ui.reservation.RemoveReservationDialogParameters
import com.google.samples.apps.iosched.ui.schedule.ScheduleTwoPaneViewModel
import com.google.samples.apps.iosched.ui.schedule.sessionDateTimeLocation
import com.google.samples.apps.iosched.ui.theme.Black
import com.google.samples.apps.iosched.ui.theme.IOTheme
import com.google.samples.apps.iosched.ui.theme.White
import com.google.samples.apps.iosched.util.openWebsiteUrl
import com.google.samples.apps.iosched.widget.Loader
import kotlinx.coroutines.flow.collect
import java.time.Duration
import java.time.ZoneId

@ExperimentalComposeUiApi
@Composable
fun SessionDetailScreen(
    sessionId: SessionId,
    onBackPress: () -> Unit,
    openSpeakerDetailScreen: (SpeakerId) -> Unit,
    openFeedbackDialog: (SessionId) -> Unit,
    openSignInDialog: () -> Unit,
    openSwapReservationDialog: (SwapRequestParameters) -> Unit,
    openYoutubeUrl: (String) -> Unit,
    openRemoveReservationDialog: (RemoveReservationDialogParameters) -> Unit,
    openNotificationsPreferenceDialog: () -> Unit,
    sessionDetailViewModel: SessionDetailViewModel = hiltViewModel(),
    scheduleTwoPaneViewModel: ScheduleTwoPaneViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    sessionDetailViewModel.setSessionId(sessionId = sessionId)
    val context = LocalContext.current
    val state by sessionDetailViewModel.sessionDetailState.collectAsState()

    IOTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp),
                    color = White,
                    contentColor = Black,
                    elevation = 8.dp,
                ) {
                    Box {
                        val image =
                            if (state.session?.hasPhoto == true) rememberCoilPainter(request = state.session?.photoUrl)
                            else painterResource(id = eventPhoto(state.session))
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = image,
                            contentDescription = "Background Image",
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                onBackPress()
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Navigation",
                                tint = Color.Gray
                            )
                        }
                    }

                }
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = White,
                    contentColor = Black,
                    cutoutShape = CircleShape,
                ) {
                    IconButton(
                        onClick = {
                            ShareCompat.IntentBuilder(context)
                                .setType("text/plain")
                                .setText(
                                    context.getString(
                                        R.string.share_text_session_detail,
                                        state.session?.title,
                                        state.session?.sessionUrl
                                    )
                                )
                                .setChooserTitle(R.string.intent_chooser_session_detail)
                                .startChooser()
                        }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = {
                            state.session?.let { session ->
                                openWebsiteUrl(context, session.doryLink)
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = "Mail",
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = {
                            state.session?.let {
                                addToCalendar(context = context, session = it)
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = Color.Gray
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        state.userSession?.let(
                            scheduleTwoPaneViewModel::onStarClicked
                        )
                    },
                    backgroundColor = White
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star_border),
                        contentDescription = null,
                        tint = Color.Gray,
                    )
                }
            },
            isFloatingActionButtonDocked = true
        ) {

            val modifier = Modifier.padding(it)

            LaunchedEffect(key1 = sessionDetailViewModel.navigationActions) {
                sessionDetailViewModel.navigationActions.collect { action ->
                    when (action) {
                        is SessionDetailNavigationAction.NavigateToSessionFeedback -> {
                            openFeedbackDialog(action.sessionId)
                        }
                        SessionDetailNavigationAction.NavigateToSignInDialogAction -> {
                            openSignInDialog()
                        }
                        is SessionDetailNavigationAction.NavigateToSpeakerDetail -> {
                            openSpeakerDetailScreen(action.speakerId)
                        }
                        is SessionDetailNavigationAction.NavigateToSwapReservationDialogAction -> {
                            openSwapReservationDialog(action.params)
                        }
                        is SessionDetailNavigationAction.NavigateToYoutube -> {
                            openYoutubeUrl(action.videoId)
                        }
                        is SessionDetailNavigationAction.RemoveReservationDialogAction -> {
                            openRemoveReservationDialog(action.params)
                        }
                        SessionDetailNavigationAction.ShowNotificationsPrefAction -> {
                            openNotificationsPreferenceDialog()
                        }
                    }
                }
            }

            if (state.loading)
                Loader(modifier = modifier)
            else
                SessionDetail(
                    modifier = modifier,
                    session = state.session,
                    relatedUserSessions = state.relatedUserSessions,
                    speakers = state.speakers,
                    zoneId = state.zoneId,
                    timeUntilStart = state.timeUntilStart,
                    onStarClick = scheduleTwoPaneViewModel::onStarClicked,
                    openEventDetail = scheduleTwoPaneViewModel::openEventDetail,
                    onSpeakerClick = sessionDetailViewModel::onSpeakerClicked
                )

        }
    }

}

@ExperimentalComposeUiApi
@Composable
private fun SessionDetail(
    modifier: Modifier,
    timeUntilStart: Duration,
    zoneId: ZoneId,
    session: Session?,
    relatedUserSessions: List<UserSession>,
    speakers: List<Speaker>,
    onStarClick: (UserSession) -> Unit,
    openEventDetail: (String) -> Unit,
    onSpeakerClick: (String) -> Unit,
) {
    LazyColumn(modifier = modifier) {

        val list = buildMergedList(
            relatedSessions = relatedUserSessions,
            sessionSpeakers = speakers
        )

        items(list) { item ->

            when (item) {
                is SessionItem -> SessionInfoCard(
                    session = session,
                    zoneId = zoneId,
                    timeUntilStart = timeUntilStart,
                )
                is Speaker -> SpeakerCard(
                    speaker = item,
                    onSpeakerClick = onSpeakerClick,
                )
                is UserSession -> SessionCard(
                    zoneId = zoneId,
                    userSession = item,
                    onStarClick = onStarClick,
                    openEventDetail = openEventDetail
                )
                is SectionHeader -> SectionHeaderCard(
                    sectionHeader = item
                )
                else -> throw IllegalStateException("Unknown item $item")
            }

        }
    }
}

@Composable
private fun SectionHeaderCard(
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
private fun SessionCard(
    zoneId: ZoneId,
    userSession: UserSession,
    onStarClick: (UserSession) -> Unit,
    openEventDetail: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                openEventDetail(userSession.session.id)
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
                onStarClick(userSession)
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

@Composable
private fun SpeakerCard(
    speaker: Speaker,
    onSpeakerClick: (String) -> Unit,
) {
    Row(modifier = Modifier
        .padding(16.dp)
        .clickable { onSpeakerClick(speaker.id) }) {
        Image(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            painter = rememberCoilPainter(request = speaker.imageUrl),
            contentDescription = "Speaker's Photo"
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier) {
            Text(
                text = speaker.name,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = speaker.company,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
private fun SessionInfoCard(
    timeUntilStart: Duration,
    zoneId: ZoneId,
    session: Session?,
) {
    session?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.title, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = timeString(
                    sessionDetailStartTime = it.startTime,
                    sessionDetailEndTime = it.endTime,
                    timeZoneId = zoneId
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier) {
                Text(text = it.room?.name ?: "")
                Icon(
                    painter = painterResource(id = R.drawable.ic_livestreamed),
                    contentDescription = "Live Stream",
                    tint = Color.Gray
                )
            }
            Text(text = it.levelTag()?.displayName ?: "")
            Text(text = "$timeUntilStart")
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = it.description)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
            ) {
                it.displayTags.forEach {
                    Tag(
                        modifier = Modifier,
                        tag = it
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Rate Session")
            }

        }
    } ?: run {
        CircularProgressIndicator()
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
                color = Color.Gray
            )
        )
        Spacer(modifier = modifier.width(10.dp))
    }
}

private fun buildMergedList(
    sessionSpeakers: List<Speaker>,
    relatedSessions: List<UserSession>
): List<Any> {
    val merged = mutableListOf<Any>(SessionItem)
    if (sessionSpeakers.isNotEmpty()) {
        merged += SectionHeader(R.string.session_detail_speakers_header)
        merged.addAll(sessionSpeakers)
    }
    if (relatedSessions.isNotEmpty()) {
        merged += SectionHeader(R.string.session_detail_related_header)
        merged.addAll(relatedSessions)
    }
    return merged
}

private fun addToCalendar(context: Context, session: Session) {
    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.Events.TITLE, session.title)
        .putExtra(CalendarContract.Events.EVENT_LOCATION, session.room?.name)
        .putExtra(
            CalendarContract.Events.DESCRIPTION,
            session.getCalendarDescription(
                context.getString(R.string.paragraph_delimiter),
                context.getString(R.string.speaker_delimiter)
            )
        )
        .putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            session.startTime.toEpochMilli()
        )
        .putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            session.endTime.toEpochMilli()
        )
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

