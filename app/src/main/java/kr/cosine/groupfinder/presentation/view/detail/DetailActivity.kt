package kr.cosine.groupfinder.presentation.view.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry.uniqueId
import kr.cosine.groupfinder.databinding.ActivityDetailBinding
import kr.cosine.groupfinder.domain.model.GroupDetailEntity
import kr.cosine.groupfinder.domain.model.GroupOwnerEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import kr.cosine.groupfinder.presentation.view.common.data.Code
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.extension.applyWhite
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.extension.showToast
import kr.cosine.groupfinder.presentation.view.detail.event.DetailEvent
import kr.cosine.groupfinder.presentation.view.dialog.Dialog
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.GroupTagItemDecoration
import kr.cosine.groupfinder.util.MyFirebaseMessagingService
import java.util.UUID

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val detailViewModel: DetailViewModel by viewModels()

    private val categoryAdapter by lazy { DetailCategoryAdapter() }

    private val laneAdapter by lazy { DetailLaneAdapter() }

    private var progressDialog: Dialog? = null
    private var isProgressDialogDismissed = false
    private lateinit var postUniqueId: UUID

    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        postUniqueId = intent.getSerializableExtra(IntentKey.POST_UNIQUE_ID) as UUID
        registerProgressBar()
        registerMenuButton()
        registerCloseButton()
        observeData()
        registerViewModelEvent()
        showProgressBar()
        detailViewModel.getPostDetail(postUniqueId)
        laneOnClick()
        registerBackPressedCallback()
    }

    private fun registerProgressBar() {
        binding.progressBar.applyWhite()
    }

    private fun registerMenuButton() {
        binding.menuImageButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            menuInflater.inflate(R.menu.post_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.report_group -> showReportGroupDialog()

                    R.id.report_user -> showReportUserDialog()

                    R.id.block_user -> showBlockUserDialog()

                    R.id.delete_post -> showDeletePostDialog()
                }
                return@setOnMenuItemClickListener true
            }
        }
        registerForContextMenu(binding.menuImageButton)
    }

    private fun observeData() {
        detailViewModel.postDetail.observe(this) { post ->
            if (post != null) {
                bindDetailInformation(post)
                bindCategoriesFromData(post.tags)
                bindLanesFromData(post.laneMap)
                showInformationView()
            } else {
                Log.d("Error", "onCreate: 비 정상적인 로딩")
                // 잘못된 게시글 로드시 에러처리 오류창 디자인 요청.
            }
        }
        detailViewModel.groupRole.observe(this) { role ->
            laneAdapter.powerUpdate(role)
            //showDeleteGroupButton(role)
            Log.d("DETAIL", "observeData: ${role}")
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
            removeItemDecoration(GroupTagItemDecoration)
            addItemDecoration(GroupTagItemDecoration)
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

    private fun showInformationView() = with(binding) {
        progressBar.visibility = View.INVISIBLE
        rootConstraintLayout.visibility = View.VISIBLE
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

            override fun onExitClick(
                view: View,
                lane: Lane,
                userUUID: UUID,
                userNickname: String,
                userTag: String
            ) {
                val title = when (detailViewModel.groupRole.value) {
                    HOST -> "강제 퇴장"
                    PARTICIPANT -> "나가기"
                    else -> return
                }
                val message = when (detailViewModel.groupRole.value) {
                    HOST -> "${userNickname}#${userTag} 유저를 추방 하시겠습니까?"
                    PARTICIPANT -> "이 방에서 나가시겠습니까?"
                    else -> return
                }
                handleExitClick(userUUID, title, message)
            }
        }
    }


    private fun handleExitClick(userUUID: UUID, title: String, message: String) {
        Dialog(
            title = title,
            message = message,
            onConfirmClick = {
                detailViewModel.postDetail.value?.let { post ->
                    MyFirebaseMessagingService().sendLeaveGroupRequest(
                        postUUID = post.postUniqueId,
                        targetUUID = userUUID
                    )
                    if (detailViewModel.groupRole.value == PARTICIPANT) {
                        setResult(Code.SUCCESS_POST_TASK)
                        finish()
                    }
                }
            }
        ).show(supportFragmentManager, Dialog.TAG)

        Log.d("test", "onExitClick: $message")
    }


    private fun showJoinRequestDialog(ownerUniqueId: UUID, postUniqueId: UUID, lane: Lane) {
        Dialog(
            title = "참가 요청",
            message = "${lane.displayName} 라인에 참가하시겠습니까?",
            onConfirmClick = {
                progressDialog = Dialog(
                    title = "참가 요청 중...",
                    message = "잠시만 기다려 주세요.",
                    cancelButtonTitle = "요청 취소",
                    confirmButtonVisibility = View.GONE,
                    onCancelClick = {
                        MyFirebaseMessagingService().cancelJoinRequest()
                        dismissProgressDialog()
                    }
                )
                isProgressDialogDismissed = false
                progressDialog!!.show(supportFragmentManager, Dialog.TAG)

                MyFirebaseMessagingService().sendJoinRequest(
                    targetUUID = ownerUniqueId,
                    senderUUID = uniqueId,
                    lane = lane,
                    postUUID = postUniqueId
                )

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (!isProgressDialogDismissed) {
                        progressDialog!!.dismiss()
                        if (!isFinishing && !isDestroyed) {
                            Dialog(
                                title = "시간 초과",
                                message = "잠시 후 다시 시도해주세요.",
                                cancelButtonVisibility = View.GONE,
                            ).show(supportFragmentManager, Dialog.TAG)
                        }
                    }
                }, 25000)
            }
        ).show(supportFragmentManager, Dialog.TAG)
    }

    private fun registerCloseButton() {
        binding.closeImageButton.setOnClickListenerWithCooldown {
            finish()
        }
    }

    /*private fun showDeleteGroupButton(role: Int) = with(binding.deleteGroupTextView) {
        visibility = if (role == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }*/

    private fun showReportGroupDialog() {
        Dialog("방 신고하기", "정말 해당 방을 신고하시겠습니까?") {
            detailViewModel.reportGroup(postUniqueId)
        }.show(supportFragmentManager, Dialog.TAG)
    }

    private fun showReportUserDialog() {
        getOwnerUniqueId {
            Dialog("작성자 신고하기", "정말 해당 작성자를 신고하시겠습니까?") {
                detailViewModel.reportUser(it)
            }.show(supportFragmentManager, Dialog.TAG)
        }
    }

    private fun showBlockUserDialog() {
        getOwnerUniqueId {
            Dialog("작성자 차단하기", "정말 해당 작성자를 차단하시겠습니까?") {
                detailViewModel.blockUser(it)
            }.show(supportFragmentManager, Dialog.TAG)
        }
    }

    private fun getOwnerUniqueId(ownerUniqueIdScope: (UUID) -> Unit) {
        val ownerUniqueId = detailViewModel.postDetail.value?.owner?.uniqueId ?: run {
            showToast("작성자 데이터를 불러오지 못했습니다.")
            return
        }
        ownerUniqueIdScope(ownerUniqueId)
    }

    private fun showDeletePostDialog() = with(binding) {
        val role = detailViewModel.groupRole.value ?: return@with
        if (role != HOST) {
            showToast("권한이 없습니다.")
            return@with
        }
        Log.d("DETAIL", "showDeletePostDialog: click!")
        Dialog(
            title = "방 삭제",
            message = "정말 방을 삭제하시겠습니까?",
            onConfirmClick = {
                showProgressBar()
                MyFirebaseMessagingService().sendDeleteGroupRequest(postUniqueId) {
                    setResult(Code.SUCCESS_POST_TASK)
                    finish()
                }
            }
        ).show(supportFragmentManager, Dialog.TAG)
    }

    private fun registerViewModelEvent() {
        lifecycleScope.launch {
            detailViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                if (event is DetailEvent.Notice) {
                    if (event is DetailEvent.Success && event !is DetailEvent.ReportUserSuccess) {
                        setResult(Code.SUCCESS_POST_TASK)
                        finish()
                    }
                    showToast(event.message)
                }
            }
        }
    }

    private fun showProgressBar() = with(binding) {
        rootConstraintLayout.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    fun dismissProgressDialog() {
        isProgressDialogDismissed = true
        progressDialog?.dismiss()
    }

    fun reFreshGroupDetail() {
        detailViewModel.getPostDetail(postUniqueId)
    }

    private fun registerBackPressedCallback() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.progressBar.visibility != View.VISIBLE) {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }
}