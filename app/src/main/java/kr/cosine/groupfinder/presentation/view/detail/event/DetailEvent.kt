package kr.cosine.groupfinder.presentation.view.detail.event

sealed interface DetailEvent {

    open class Notice(
        open val message: String
    ) : DetailEvent

    data object AlreadyReportGroup : Notice("이미 신고한 방입니다.")

    data object AlreadyRepostUser : Notice("이미 신고한 유저입니다.")

    data object AlreadyBlockUser : Notice("이미 차단한 유저입니다.")

    data object UnknownFail : Notice("알 수 없는 오류로 실패하였습니다.")

    open class Success(
        override val message: String
    ) : Notice(message)

    data object ReportGroupSuccess : Success("해당 방을 신고하였습니다.")

    data object ReportUserSuccess : Success("해당 유저를 신고하였습니다.")

    data object BlockUserSuccess : Success("해당 유저를 차단하였습니다.")
}