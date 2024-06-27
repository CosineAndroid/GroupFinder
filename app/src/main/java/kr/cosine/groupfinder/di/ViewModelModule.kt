package kr.cosine.groupfinder.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.repository.AccountRepositoryImpl
import kr.cosine.groupfinder.data.repository.BroadcastRepositoryImpl
import kr.cosine.groupfinder.data.repository.GroupRepositoryImpl
import kr.cosine.groupfinder.data.repository.LoginSessionRepositoryImpl
import kr.cosine.groupfinder.data.repository.PostRepositoryImpl
import kr.cosine.groupfinder.data.repository.RiotRepositoryImpl
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.domain.repository.BroadcastRepository
import kr.cosine.groupfinder.domain.repository.GroupRepository
import kr.cosine.groupfinder.domain.repository.LoginSessionRepository
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.domain.repository.RiotRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ) : AccountRepository

    @Binds
    @ViewModelScoped
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @ViewModelScoped
    abstract fun bindRiotRepository(
        riotRepositoryImpl: RiotRepositoryImpl
    ): RiotRepository

    @Binds
    @ViewModelScoped
    abstract fun bindGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    @ViewModelScoped
    abstract fun bindLoginSessionRepository(
        loginSessionRepositoryImpl: LoginSessionRepositoryImpl
    ): LoginSessionRepository

    @Binds
    @ViewModelScoped
    abstract fun bindBroadcastRepository(
        broadcastRepositoryImpl: BroadcastRepositoryImpl
    ) : BroadcastRepository
}