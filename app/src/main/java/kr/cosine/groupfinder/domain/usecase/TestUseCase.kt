package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.TestEntity
import kr.cosine.groupfinder.domain.repository.TestRepository
import javax.inject.Inject

@ViewModelScoped
class TestUseCase @Inject constructor(
    private val testRepository: TestRepository
) {

    operator fun invoke(): TestEntity {
        return testRepository.onTest().toEntity()
    }
}