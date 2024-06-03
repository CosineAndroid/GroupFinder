package kr.cosine.groupfinder.presentation.view.account.login.event

sealed interface LoginEvent {

    data object Success : LoginEvent

    data object InvalidAccount : LoginEvent
}