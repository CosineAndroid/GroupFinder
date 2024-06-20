package kr.cosine.groupfinder.presentation.view.broadcast.event

sealed interface BroadcastDraftEvent {

    val message: String

    data object Fail : BroadcastDraftEvent {
        override val message = "알 수 없는 오류로 실패하였습니다."
    }

    data object Success : BroadcastDraftEvent {
        override val message = "공지사항을 등록하였습니다."
    }
}