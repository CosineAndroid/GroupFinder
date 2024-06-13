package kr.cosine.groupfinder.presentation.view.write.event

import kr.cosine.groupfinder.domain.model.PostEntity

sealed interface WriteEvent {

    open class Notice(
        val message: String
    ) : WriteEvent

    data object AlreadyJoinFail : Notice("이미 참가 중인 방이 있습니다.")

    data object UnknownFail : Notice("알 수 없는 오류로 실패하였습니다.")

    data class Success(
        val postEntity: PostEntity
    ) : WriteEvent
}