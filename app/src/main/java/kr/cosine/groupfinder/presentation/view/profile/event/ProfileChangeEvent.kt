package kr.cosine.groupfinder.presentation.view.profile.event

sealed interface ProfileChangeEvent {

    open class Notice(
        val message: String
    ) : ProfileChangeEvent

    data object NicknameBlankFail : Notice("닉네임은 공백이 될 수 없습니다.")

    data object TagBlankFail : Notice("태그는 공백이 될 수 없습니다.")

    data object AlreadyExistsTaggedNicknameFail : Notice("이미 존재하는 닉네임#태그입니다.")

    data object UnknownFail : Notice("알 수 없는 오류로 변경에 실패하였습니다.")

    data class Success(
        val nickname: String,
        val tag: String
    ) : Notice("$nickname#$tag(으)로 변경되었습니다.")
}