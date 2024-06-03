package kr.cosine.groupfinder.presentation.view.account.register.event

interface RegisterEvent {

    data class Success(
        val id: String,
        val password: String
    ) : RegisterEvent

    data object IdDuplicationFail : RegisterEvent

    data object TaggedNicknameDuplicationFail : RegisterEvent

    data object UnknownFail : RegisterEvent
}