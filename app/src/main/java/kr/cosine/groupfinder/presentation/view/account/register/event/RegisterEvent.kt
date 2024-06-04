package kr.cosine.groupfinder.presentation.view.account.register.event

import kr.cosine.groupfinder.domain.model.AccountEntity

interface RegisterEvent {

    data class Success(
        val accountEntity: AccountEntity
    ) : RegisterEvent

    open class Notice(
        val message: String
    ) : RegisterEvent

    data object IdDuplication : Notice("중복되는 아이디입니다.")

    data object TaggedNicknameDuplication : Notice("중복되는 닉네임#태그입니다.")

    data object Unknown : Notice("알 수 없는 오류가 발생하였습니다.")
}