package kr.cosine.groupfinder.presentation.view.profile.event

sealed interface ProfileEvent {

    open class Notice(
        val message: String
    ) : ProfileEvent

    data object UnknownFail : Notice("알 수 없는 오류로 실패하였습니다.")

    data object Success : ProfileEvent
}