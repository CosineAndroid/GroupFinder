package kr.cosine.groupfinder.presentation.view.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.databinding.ActivityDetailBinding
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import kr.cosine.groupfinder.enums.TestGlobalUserData.userID
import java.util.UUID

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val detailViewModel: DetailViewModel by viewModels()

    private val categoryAdapter by lazy { DetailCategoryAdapter() }

    private val laneAdapter by lazy { DetailLaneAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
        detailViewModel.getTest()
        laneOnClick()
    }

    private fun test() {
        val testid = "f22b0151-5145-42ad-bbfb-4272b23fa57f"
        val uuid = UUID.fromString(testid)
        detailViewModel.getPostDetail(uuid)
    }

    private fun observeData() {
        detailViewModel.postDetail.observe(this) { post ->
            if (post != null) {
                bindDetailInformation(post)
                bindCategoriesFromData(post.tags)
                //bindLanesFromData(post.laneMap)
            } else {
                Log.d("Error", "onCreate: 비 정상적인 로딩")
                // 잘못된 게시글 로드시 에러처리 오류창 디자인 요청.
            }
        }
        detailViewModel.groupRole.observe(this) { role ->
            laneAdapter.powerUpdate(role)
        }
    }

    private fun bindDetailInformation(postEntity: PostEntity) {
        with(binding) {
            titleTextView.text = postEntity.title
            // idTextView.text = postEntity.id
            memoTextView.text = postEntity.body
        }
    }

    private fun bindCategoriesFromData(tags: List<String>) {
        with(binding.tags) {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            //addItemDecoration(TagItemDecoration(right = 5, bottom = 0))
        }
        categoryAdapter.categoriesUpdate(tags)
    }

    private fun bindLanesFromData(lanes: Map<Lane, String?>) {
        with(binding.lanes) {
            adapter = laneAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        laneAdapter.laneUpdate(lanes)
    }

    private fun laneOnClick() {
        laneAdapter.itemClick = object : DetailLaneAdapter.ItemClick {
            override fun onClick(view: View, lane: Lane) {
                Log.d("test", "onClick: ${lane}, Empty") // 참가요청 보내는 로직
            }

            override fun onExitClick(view: View, lane: Lane, userName: String?) {
                val userRole = detailViewModel.groupRole.value
                val loginUserName = userID
                if (userRole == HOST) {
                    if (loginUserName == userName) {
                        Log.d("test", "onExitClick: 방을 닫겠습니까?")
                    } else {
                        Log.d("test", "onExitClick: ${userName} 유저를 강퇴 하시겠습니까?")
                    }
                } else if (userRole == PARTICIPANT) {
                    Log.d("test", "onExitClick: 방을 나가겠습니까?")
                }
            }
        }
    }


}