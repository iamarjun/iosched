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

package com.google.samples.apps.iosched.ui.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.samples.apps.iosched.R
import com.google.samples.apps.iosched.model.Block
import com.google.samples.apps.iosched.ui.MainActivityViewModel
import com.google.samples.apps.iosched.ui.theme.Black
import com.google.samples.apps.iosched.ui.theme.IOTheme
import com.google.samples.apps.iosched.ui.theme.White
import java.time.ZoneId

@Composable
fun AgendaScreen(
    mainViewModel: MainActivityViewModel = viewModel(),
    viewModel: AgendaViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    //TODO: Once the full migration is done, move this to the top level
    IOTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Agenda",
                            style = MaterialTheme.typography.h6
                        )
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
                    elevation = 8.dp
                )
            }
        ) {

            val modifier = Modifier.padding(it)

            val agendas by viewModel.agenda.collectAsState()
            val zoneId by viewModel.timeZoneId.collectAsState()

            Agendas(
                modifier = modifier,
                agendas = agendas,
                zoneId = zoneId,
            )

        }
    }
}

@Composable
private fun Agendas(
    modifier: Modifier,
    agendas: List<Block>,
    zoneId: ZoneId
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {

        indexAgendaHeaders(agendas, zoneId).forEach { (header, list) ->

            item {
                AgendaHeader(
                    modifier = modifier,
                    stringRes = header
                )
            }

            items(list) { agenda ->
                Agenda(
                    modifier = modifier,
                    agenda = agenda,
                    zoneId = zoneId
                )
            }

        }
    }
}

@Composable
private fun AgendaHeader(
    modifier: Modifier,
    stringRes: Int
) {
    Text(
        modifier = modifier
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth(),
        text = stringResource(id = stringRes),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6
    )
}

@Composable
private fun Agenda(
    modifier: Modifier,
    agenda: Block,
    zoneId: ZoneId
) {
    Surface(
        modifier = modifier.padding(horizontal = 16.dp),
        color = Color(agenda.color),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.agenda_item_stroke_width),
            color = Color(agenda.strokeColor)
        )
    ) {
        Row(
            modifier = modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 6.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {

            Icon(
                modifier = modifier
                    .padding(8.dp)
                    .size(24.dp),
                painter = painterResource(id = agendaIcon(agenda.type)),
                contentDescription = "Agenda Icon"
            )

            Column(
                modifier = modifier
            ) {
                Text(
                    text = agenda.title,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = agendaDuration(agenda, zoneId),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

