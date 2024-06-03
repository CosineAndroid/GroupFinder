package kr.cosine.groupfinder.presentation.view.account.register.event

import kr.cosine.groupfinder.domain.model.AccountEntity

interface RegisterEvent {

    data class Success(
        val accountEntity: AccountEntity
    ) : RegisterEvent

    data object IdDuplicationFail : RegisterEvent

    data object TaggedNicknameDuplicationFail : RegisterEvent

    data object UnknownFail : RegisterEvent
}