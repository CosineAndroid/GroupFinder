package kr.cosine.groupfinder.presentation.view.account.login.event

import kr.cosine.groupfinder.domain.model.AccountEntity

sealed interface LoginEvent {

    data class Success(
        val accountEntity: AccountEntity
    ) : LoginEvent

    data object Fail : LoginEvent
}