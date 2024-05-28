package kr.cosine.krossfit.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.krossfit.domain.mapper.toEntity
import kr.cosine.krossfit.domain.model.TestEntity
import kr.cosine.krossfit.domain.repository.TestRepository
import javax.inject.Inject

@ViewModelScoped
class TestUseCase @Inject constructor(
    private val testRepository: TestRepository
) {

    operator fun invoke(): TestEntity {
        return testRepository.onTest().toEntity()
    }
}