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

package com.google.samples.apps.iosched.ui.schedule

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.inSpans
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.samples.apps.iosched.R
import com.google.samples.apps.iosched.model.ConferenceDay
import com.google.samples.apps.iosched.model.Tag
import com.google.samples.apps.iosched.model.userdata.UserSession
import com.google.samples.apps.iosched.ui.MainActivityViewModel
import com.google.samples.apps.iosched.ui.theme.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm")
private val meridiemFormatter = DateTimeFormatter.ofPattern("a")
private val timeTextSizeSpan = AbsoluteSizeSpan(20)
private val meridiemTextSizeSpan = AbsoluteSizeSpan(14)
private val boldSpan = StyleSpan(Typeface.BOLD)

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    mainViewModel: MainActivityViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    //TODO: Once the full migration is done, move this to the top level
    IOTheme {
        ProvideWindowInsets {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    val days = mutableListOf<DayIndicator>(
                        DayIndicator(
                            day = ConferenceDay(
                                start = ZonedDateTime.now(),
                                end = ZonedDateTime.now().plusDays(1),
                            )
                        ),
                        DayIndicator(
                            day = ConferenceDay(
                                start = ZonedDateTime.now().plusDays(1),
                                end = ZonedDateTime.now().plusDays(2)
                            )
                        ),
                        DayIndicator(
                            day = ConferenceDay(
                                start = ZonedDateTime.now().plusDays(2),
                                end = ZonedDateTime.now().plusDays(3)
                            )
                        )
                    )

                    val listState = rememberLazyListState()
                    var selectedIndex by remember { mutableStateOf(-1) }
                    TopAppBar(
                        title = {
                            LazyRow(
                                state = listState
                            ) {
                                itemsIndexed(days) { index, item ->
                                    Surface(
                                        color = if (index == selectedIndex) Teal else Transparent,
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = "Day ${index + 1}",
                                            modifier = Modifier
                                                .selectable(
                                                    selected = index == selectedIndex,
                                                    onClick = {
                                                        selectedIndex = if (selectedIndex != index)
                                                            index else -1
                                                    }
                                                )
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                ),
                                            style = TextStyle(
                                                color = if (index == selectedIndex) White else Black,
                                            )
                                        )
                                    }

                                }
                            }
                        },
                        actions = {

                            IconButton(onClick = { mainViewModel.onProfileClicked() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_default_profile_avatar),
                                    contentDescription = "Profile",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        },
                        backgroundColor = White,
                        contentColor = Black,
                        elevation = 8.dp,
                        modifier = Modifier.statusBarsPadding()
                    )
                }
            ) {

                val modifier = Modifier.padding(it)


                val scheduleUiData by viewModel.scheduleUiData.collectAsState()

                if (scheduleUiData.isLoading())
                    CircularProgressIndicator()
                else
                    Schedules(
                        modifier = modifier,
                        schedules = scheduleUiData.list!!,
                        zoneId = scheduleUiData.timeZoneId!!,
                    )

            }
        }
    }

}


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun Schedules(
    modifier: Modifier,
    schedules: List<UserSession>,
    zoneId: ZoneId
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val sessions = schedules.groupBy { it.session.startTime.truncatedTo(ChronoUnit.MINUTES) }

        sessions.forEach { (startTime, sessions) ->

            stickyHeader {
                val text = SpannableStringBuilder().apply {
                    inSpans(timeTextSizeSpan) {
                        append(timeFormatter.format(startTime))
                    }
                    append(System.lineSeparator())
                    inSpans(meridiemTextSizeSpan, boldSpan) {
                        append(meridiemFormatter.format(startTime).toUpperCase(Locale.getDefault()))
                    }
                }

                Text(
                    modifier = modifier.width(84.dp),
                    text = text.toString(),
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }

            items(sessions) { session ->
                Schedule(
                    modifier = modifier,
                    userSession = session,
                    zoneId = zoneId
                )
            }

        }
    }

}

@ExperimentalComposeUiApi
@Composable
private fun Schedule(
    modifier: Modifier,
    userSession: UserSession,
    zoneId: ZoneId?,
) {
    Column(
        modifier = modifier.padding(start = 84.dp, end = 16.dp)
    ) {

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = userSession.session.title,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.W600)
                )
                Row(
                    modifier = modifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reserved),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(
                        text = "RESERVE SEAT",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color.Gray
                        )
                    )
                }

                Row(
                    modifier = modifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_livestreamed),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                    Spacer(modifier = modifier.width(10.dp))
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

            Icon(
                modifier = modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_agenda_keynote),
                contentDescription = null
            )
        }
//        Row {
//            userSession.session.displayTags.forEach {
//                Tag(
//                    modifier = modifier,
//                    tag = it
//                )
//            }
//        }
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