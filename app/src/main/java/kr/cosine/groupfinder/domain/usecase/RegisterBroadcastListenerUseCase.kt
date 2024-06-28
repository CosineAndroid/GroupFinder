package kr.cosine.groupfinder.domain.usecase

import android.app.Activity
import com.google.firebase.firestore.DocumentChange
import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.BroadcastRepository
import javax.inject.Inject

@ViewModelScoped
class RegisterBroadcastListenerUseCase @Inject constructor(
    private val broadcastRepository: BroadcastRepository
) {

    operator fun invoke(activity: Activity, onBroadcast: () -> Unit) {
        broadcastRepository.addSnapshotListener(activity) { querySnapshot ->
            val isAdded = querySnapshot.documentChanges.any {
                it.type == DocumentChange.Type.ADDED
            }
            if (isAdded) {
                onBroadcast()
            }
        }
    }
}