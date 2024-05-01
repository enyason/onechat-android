package com.enyason.onechat.ui.rooms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enyason.onechat.data.remote.models.Room
import com.enyason.onechat.ui.Screen


@Composable
fun RoomsScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<RoomsViewModel>()
    val state = viewModel.rooms.collectAsState()

    RoomsList(rooms = state.value) { roomid ->
        navController.navigate(Screen.RoomChat(roomid).route)
    }
}

@Composable
fun RoomsList(
    rooms: List<Room>,
    onRoomClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(rooms) { room ->
            RoomItem(room = room, onRoomClick = onRoomClick)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun RoomItem(
    room: Room,
    onRoomClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRoomClick(room.id) },
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Created by: ${room.creator}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
