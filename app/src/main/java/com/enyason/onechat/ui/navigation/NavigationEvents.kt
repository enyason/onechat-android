package com.enyason.onechat.ui.navigation


sealed class Event
sealed class NavigationEvent : Event() {
    data object LoggedInNavigationEvent : NavigationEvent()
    data object RegisterNavigationEvent : NavigationEvent()
    data object RoomsNavigationEvent : NavigationEvent()
    data object RoomChatNavigationEvent : NavigationEvent()
}
