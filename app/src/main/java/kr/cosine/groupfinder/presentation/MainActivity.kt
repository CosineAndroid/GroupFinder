package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.databinding.ActivityMainBinding
import kr.cosine.groupfinder.presentation.view.test.model.PostViewModel

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
                    val postResponse = documentChange.document.toObject(PostResponse::class.java)
                    Log.d("GroupFinderTest", "[ADDED] PostModel : $postResponse")
                }
            }
        }
        registerView()
    }

    private fun registerView() {
        binding.sendButton.setOnClickListener {
            postViewModel.createPost(listOf("태그1", "태그2", "태그4"))
            Log.d("GroupFinderTest2", "눌렀음")
            postViewModel.getPosts() { list ->
                list.forEach {
                    Log.d("GroupFinderTest2", it.toString())
                }
            }
        }
    }
}