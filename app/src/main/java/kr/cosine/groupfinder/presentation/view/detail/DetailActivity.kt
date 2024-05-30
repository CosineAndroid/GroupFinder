package kr.cosine.groupfinder.presentation.view.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.databinding.ActivityDetailBinding
import java.util.UUID

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private lateinit var firestore: FirebaseFirestore

    private val detailViewModel: DetailViewModel by viewModels()

    private val categoryAdapter by lazy { DetailCategoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        detailViewModel.postDetail.observe(this, Observer { post ->
            if (post != null) {
                bindDetailInformation(post)
                bindCategoriesFromData(post.tags)
            } else {
                Log.d("Error", "onCreate: 비 정상적인 로딩")
                // 잘못된 게시글 로드시 에러처리 오류창 디자인 요청.
            }
        })
        test()
    }

    private fun test() {
        val testid = "f22b0151-5145-42ad-bbfb-4272b23fa57f"
        val uuid = UUID.fromString(testid)
        detailViewModel.getPostDetail(uuid)
    }

    private fun bindDetailInformation(postModel: PostModel) {
        with(binding) {
            titleTextView.text = postModel.title
            idTextView.text = postModel.id
            memoTextView.text = postModel.body
        }
    }

    private fun bindCategoriesFromData(tags: List<String>) {
        with(binding.tags) {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        categoryAdapter.categoriesUpdate(tags)
    }

}