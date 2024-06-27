package kr.cosine.groupfinder.presentation.view.broadcast.event

interface BroadcastListEvent {

    val message: String

    data object Fail : BroadcastListEvent {
        override val message = "알 수 없는 오류로 실패하였습니다."
    }

    data object Success : BroadcastListEvent {
        override val message = "해당 공지사항을 제거하였습니다."
    }
}