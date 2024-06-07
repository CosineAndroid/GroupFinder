package kr.cosine.groupfinder.presentation.view.write

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.databinding.ActivityWriteBinding
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.common.adapter.TagAdapter
import kr.cosine.groupfinder.presentation.view.common.code.Code
import kr.cosine.groupfinder.presentation.view.common.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.common.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.list.event.TagEvent
import kr.cosine.groupfinder.presentation.view.search.SearchFragment
import kr.cosine.groupfinder.presentation.view.write.adapter.RequireLaneRecyclerViewAdapter
import kr.cosine.groupfinder.presentation.view.write.adapter.SpinnerAdapter
import java.util.UUID

@AndroidEntryPoint
class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding

    private lateinit var mode: Mode

    private lateinit var tagRecyclerViewAdapter: TagAdapter
    lateinit var requireLaneRecyclerViewAdapter: RequireLaneRecyclerViewAdapter

    private val requireLaneList = mutableListOf(RequireLane("0"))
    private val defaultLane = LaneSpinnerItem.laneItems[0].lane
    private var selectedMyLane = defaultLane

    private val tagViewModel by viewModels<TagViewModel>()
    private val writeViewModel by viewModels<WriteViewModel>()

    private val flexboxTagDecorationDrawable by lazy {
        AppCompatResources.getDrawable(
            this@WriteActivity,
            R.drawable.flexbox_tag_decoration
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mode = intent.getSerializableExtra(IntentKey.MODE) as Mode

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.writeActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initSpinner()
        registerTagFragment()
        setupTagRecyclerViewAdapter()
        setupRequireLaneRecyclerViewAdapter()
        setOnAddLaneButtonListener()
        setOnCreateRoomClickListener()
        registerTagViewModel()
        addTagsButton()
        setGameModeText()

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }



    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val writeInSearchLayout = binding.writeInSearchLayout
            if (writeInSearchLayout.isVisible) {
                writeInSearchLayout.isVisible = false
            } else {
                finish()
            }
        }
    }

    private fun setGameModeText(){
        binding.gameModeTextView.text = mode.displayName
    }


    private fun registerTagFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.writeInSearchLayout, SearchFragment())
            .commit()
    }

    private fun setupTagRecyclerViewAdapter() = with(binding.writeTagRecyclerView) {
        adapter = TagAdapter(tagViewModel.tags.toMutableList(), tagViewModel::removeTag).apply {
            tagRecyclerViewAdapter = this
        }

        val flexboxItemDecoration = FlexboxItemDecoration(this@WriteActivity).apply {
            setDrawable(flexboxTagDecorationDrawable)
        }
        addItemDecoration(flexboxItemDecoration)

        layoutManager = FlexboxLayoutManager(this@WriteActivity).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            flexWrap = FlexWrap.WRAP
        }
    }


    private fun registerTagViewModel() {
        lifecycleScope.launch {
            tagViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is TagEvent.SetTag -> tagRecyclerViewAdapter.setTags(event.tags)
                    is TagEvent.AddTag -> tagRecyclerViewAdapter.addTag(event.tag)
                    is TagEvent.RemoveTag -> tagRecyclerViewAdapter.removeTag(event.tag)
                }
            }
        }
    }

    //태그 더하기
    private fun addTagsButton() = with(binding) {
        tagbackgroundCardView.setOnClickListener {
            writeInSearchLayout.isVisible = !writeInSearchLayout.isVisible
        }
    }

    //리싸이클러뷰 어댑터
    private fun setupRequireLaneRecyclerViewAdapter() {
        requireLaneRecyclerViewAdapter = RequireLaneRecyclerViewAdapter(requireLaneList)
        binding.requireLanesRecyclerView.apply {
            adapter = requireLaneRecyclerViewAdapter
            itemAnimator = null
        }
    }

    //스피너 설정
    private fun initSpinner() {
        val myLaneSpinner = binding.selectMyLaneSpinner
        val items = LaneSpinnerItem.laneItems
        val adapter = SpinnerAdapter(this, items)
        myLaneSpinner.adapter = adapter
        myLaneSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedMyLane = items[position].lane
                if (selectedMyLane != defaultLane) {
                    this@WriteActivity.selectedMyLane = selectedMyLane
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //라인 더하는 기능
    private fun setOnAddLaneButtonListener() {
        binding.addLaneCardView.setOnClickListener {
            if (requireLaneRecyclerViewAdapter.itemCount >= 4) {
                Toast.makeText(this, "더 이상 라인을 추가할 수 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                requireLaneRecyclerViewAdapter.addLane("1")

            }
        }
    }

    //생성하기 버튼
    private fun setOnCreateRoomClickListener() {
        binding.createRoomButton.setOnClickListener {
            val hasDuplicateLanes = checkDuplicateLanes()
            val hasDefaultLane = checkDefaultLane()
            val title = binding.titleEditTextView.text.toString()

            if (title.isBlank()) {
                Toast.makeText(this, "제목이 입력되지 않았습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (hasDefaultLane) {
                Toast.makeText(this, "설정하지 않은 라인이 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (hasDuplicateLanes) {
                Toast.makeText(this, "중복된 라인이 존재합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 게시글 생성
            val requireLaneSelected = requireLaneRecyclerViewAdapter.getLanes().map { it.lane }
            val body = binding.bodyEditTextView.text.toString()
            val tags = tagViewModel.tags
            val ownerUniqueId = LocalAccountRegistry.uniqueId
            val selectedMyLaneText = selectedMyLane

            val selectedMyLane = Lane.getLaneByDisplayName(selectedMyLaneText)
            val lanes: MutableMap<Lane, UUID?> =
                (requireLaneSelected + selectedMyLaneText).associate {
                    Lane.getLaneByDisplayName(it) to null
                }.toMutableMap()

            lanes[selectedMyLane] = ownerUniqueId
            writeViewModel.createPost(mode, title, body, ownerUniqueId, tags, lanes)
            Toast.makeText(this, "생성이 완료되었습니다", Toast.LENGTH_SHORT).show()
            setResult(Code.SUCCESS_CREATE_POST)
            finish()
        }
    }

    //중복된 라인 체크
    private fun checkDuplicateLanes(): Boolean {
        val requireLaneSelected = requireLaneRecyclerViewAdapter.getLanes().map { it.lane }
        val allLanes = requireLaneSelected + selectedMyLane
        val laneSet = allLanes.toMutableSet()
        return allLanes.size != laneSet.size
    }

    //라인선택 했는지 체크
    private fun checkDefaultLane(): Boolean {
        val requireLanes = requireLaneRecyclerViewAdapter.getLanes().map { it.lane }
        val allLanes = requireLanes + selectedMyLane
        return allLanes.contains(defaultLane)
    }
}