package kr.cosine.groupfinder.presentation.view.account.login.event

import kr.cosine.groupfinder.domain.model.AccountEntity

sealed interface LoginEvent {

    open class Notice(
        val message: String
    ) : LoginEvent

    data object InvalidIdAndPasswordFail : Notice("아이디 또는 비밀번호가 올바르지 않습니다.")

    data object InvalidAccountFail : Notice("존재하지 않는 계정입니다.")

    data object IdBlankFail : Notice("아이디를 입력해주세요.")

    data object PasswordBlankFail : Notice("비밀번호를 입력해주세요.")

    data object UnknownFail : Notice("알 수 없는 오류")

    data class Success(
        val accountEntity: AccountEntity
    ) : LoginEvent
}