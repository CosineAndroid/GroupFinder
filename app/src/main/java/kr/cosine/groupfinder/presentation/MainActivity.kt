package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.databinding.ActivityMainBinding
import kr.cosine.groupfinder.presentation.view.test.model.PostViewModel
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var firestore: FirebaseFirestore

    private val postViewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firestore = Firebase.firestore
        firestore.collection("posts").addSnapshotListener { it, _ ->
            val querySnapshot = it ?: return@addSnapshotListener
            if (querySnapshot.metadata.isFromCache) return@addSnapshotListener
            querySnapshot.documentChanges.forEach { documentChange ->
                if (documentChange.type == DocumentChange.Type.ADDED) {
                    val postModel = documentChange.document.toObject(PostModel::class.java)
                    Log.d("GroupFinderTest", "[ADDED] PostModel : $postModel")
                }
            }
        }
        registerView()
    }

    private fun registerView() {
        binding.button.setOnClickListener {
            postViewModel.createPost(listOf("태그1", "태그2", "태그4"))
            Log.d("GroupFinderTest2", "눌렀음")
            /*postViewModel.getPosts { list ->
                list.forEach {
                    Log.d("GroupFinderTest2", it.toString())
                }
            }*/
            /*val uniqueId = UUID.randomUUID()
            val uniqueIdText = "$uniqueId"
            val postModel = PostModel(uniqueIdText, "ㅎㅇ", "${(1..10000000).random()}", "아이디", listOf("태그1", "태그2"), emptyMap())
            val postReference = firestore.getReference("posts")
            lifecycleScope.launch {
                postReference.child(uniqueIdText).setValue(postModel).await()
                Log.d("GroupFinderTest", "데이터 GET: ${postReference.get().await()
                    .child(uniqueIdText)
                    .getValue(PostModel::class.java)}"
                )
                Log.d("GroupFinderTest", "")
                postReference.get().await().children.forEach {
                    Log.d("GroupFinderTest", "데이터 GETS: ${it.getValue(PostModel::class.java)}}")
                    it.key?.let { key ->
                        postReference.child(key).removeValue()
                    }
                }
                postReference.get().await().children.forEach {
                    Log.d("GroupFinderTest", "데이터 GETS2: ${it.getValue(PostModel::class.java)}}")
                }
                Log.d("GroupFinderTest", "")
            }*/
        }
    }
}