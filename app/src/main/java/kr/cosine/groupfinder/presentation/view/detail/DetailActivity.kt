package kr.cosine.groupfinder.presentation.view.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry.uniqueId
import kr.cosine.groupfinder.databinding.ActivityDetailBinding
import kr.cosine.groupfinder.domain.model.GroupDetailEntity
import kr.cosine.groupfinder.domain.model.GroupOwnerEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.dialog.Dialog
import kr.cosine.groupfinder.util.MyFirebaseMessagingService
import java.util.UUID

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val detailViewModel: DetailViewModel by viewModels()

    private val categoryAdapter by lazy { DetailCategoryAdapter() }

    private val laneAdapter by lazy { DetailLaneAdapter() }

    private var progressDialog: AlertDialog? = null
    private var isProgressDialogDismissed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val postUniqueId = intent.getSerializableExtra(IntentKey.POST_UNIQUE_ID) as UUID
        observeData()
        detailViewModel.getPostDetail(postUniqueId)
        laneOnClick()
    }


    private fun observeData() {
        detailViewModel.postDetail.observe(this) { post ->
            if (post != null) {
                bindDetailInformation(post)
                bindCategoriesFromData(post.tags)
                bindLanesFromData(post.laneMap)
            } else {
                Log.d("Error", "onCreate: 비 정상적인 로딩")
                // 잘못된 게시글 로드시 에러처리 오류창 디자인 요청.
            }
        }
        detailViewModel.groupRole.observe(this) { role ->
            laneAdapter.powerUpdate(role)
        }
    }

    private fun bindDetailInformation(groupDetailEntity: GroupDetailEntity) {
        with(binding) {
            titleTextView.text = groupDetailEntity.title
            idTextView.text = "${groupDetailEntity.owner.nickname}#${groupDetailEntity.owner.tag}"
            memoTextView.text = groupDetailEntity.body
        }
    }

    private fun bindCategoriesFromData(tags: List<String>) {
        with(binding.tags) {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        categoryAdapter.categoriesUpdate(tags)
    }

    private fun bindLanesFromData(lanes: Map<Lane, GroupOwnerEntity?>) {
        with(binding.lanes) {
            adapter = laneAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        laneAdapter.laneUpdate(lanes)
    }

    private fun laneOnClick() {
        laneAdapter.itemClick = object : DetailLaneAdapter.ItemClick {
            override fun onClick(view: View, lane: Lane) {
                view.setOnClickListenerWithCooldown(1000) {
                    Log.d("test", "onClick: click!")
                    detailViewModel.postDetail.value?.let { post ->
                        showJoinRequestDialog(
                            ownerUniqueId = post.owner.uniqueId,
                            postUniqueId = post.postUniqueId,
                            lane = lane
                        )
                    } ?: run {
                        Log.e("test", "ownerUniqueId is null, unable to send join request")
                    }
                }
            }


            override fun onExitClick(view: View, lane: Lane, userUUID: UUID) {
                when (detailViewModel.groupRole.value) {
                    HOST -> {
                        if (uniqueId == userUUID) {
                            Log.d("test", "onExitClick: 방을 닫겠습니까?")
                        } else {
                            Log.d("test", "onExitClick: $userUUID 유저를 강퇴 하시겠습니까?")
                        }
                    }

                    PARTICIPANT -> {
                        Log.d("test", "onExitClick: 방을 나가겠습니까?")
                    }

                    else -> return
                }

                detailViewModel.postDetail.value?.let { post ->
                    MyFirebaseMessagingService().sendLeaveGroupRequest(
                        postUUID = post.postUniqueId,
                        targetUUID = userUUID
                    )
                }
            }

        }
    }

    private fun showJoinRequestDialog(ownerUniqueId: UUID, postUniqueId: UUID, lane: Lane) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("참가 요청")
        builder.setMessage("${lane.displayName} 라인에 참가하시겠습니까?")
        builder.setPositiveButton("예") { _, _ ->
            val progressBar = ProgressBar(this)
            progressDialog = AlertDialog.Builder(this)
                .setTitle("참가 요청 중...")
                .setView(progressBar)
                .setCancelable(false)
                .create()

            isProgressDialogDismissed = false
            progressDialog?.show()

            MyFirebaseMessagingService().sendJoinRequest(
                targetUUID = ownerUniqueId,
                senderUUID = uniqueId,
                lane = lane,
                postUUID = postUniqueId
            )

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (!isProgressDialogDismissed) {
                    progressDialog?.dismiss()
                    if(!isFinishing && !isDestroyed) { //중간에 다른 어플을 사용하다 온 경우 안전을 위함
                        AlertDialog.Builder(this)
                            .setTitle("시간 초과")
                            .setMessage("잠시 후 다시 시도해주세요.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }, 25000)
        }
        builder.setNegativeButton("아니오") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun dismissProgressDialog() {
        isProgressDialogDismissed = true
        progressDialog?.dismiss()
    }

    fun reFreshGroupDetail(postUniqueId: UUID) {
        detailViewModel.getPostDetail(postUniqueId)
    }
}