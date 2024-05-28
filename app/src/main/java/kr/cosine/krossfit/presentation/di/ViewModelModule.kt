package kr.cosine.krossfit.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.krossfit.data.repository.TestRepositoryImpl
import kr.cosine.krossfit.domain.repository.TestRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository
}