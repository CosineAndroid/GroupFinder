package kr.cosine.groupfinder.presentation.view.write

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.databinding.ActivityWriteBinding
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.common.GroupFinderActivity
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.data.ResultCode
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.flexbox.decoration.FlexboxItemDecoration
import kr.cosine.groupfinder.presentation.view.common.flexbox.manager.FlexboxLayoutManager
import kr.cosine.groupfinder.presentation.view.tag.adapter.TagAdapter
import kr.cosine.groupfinder.presentation.view.tag.event.TagEvent
import kr.cosine.groupfinder.presentation.view.tag.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.tag.sheet.TagBottomSheetFragment
import kr.cosine.groupfinder.presentation.view.write.adapter.GameModeSpinnerAdapter
import kr.cosine.groupfinder.presentation.view.write.adapter.RequireLaneRecyclerViewAdapter
import kr.cosine.groupfinder.presentation.view.write.adapter.SpinnerAdapter
import kr.cosine.groupfinder.presentation.view.write.event.WriteEvent
import kr.cosine.groupfinder.presentation.view.write.model.WriteViewModel
import java.util.UUID

@AndroidEntryPoint
class WriteActivity : GroupFinderActivity() {

    private lateinit var binding: ActivityWriteBinding

    private lateinit var mode: Mode

    private lateinit var tagRecyclerViewAdapter: TagAdapter
    private lateinit var requireLaneRecyclerViewAdapter: RequireLaneRecyclerViewAdapter

    private val requireLaneList = mutableListOf(RequireLane("0"))
    private val defaultLane = LaneSpinnerItem.laneItems[0].lane
    private var selectedMyLane = defaultLane

    private val tagViewModel by viewModels<TagViewModel>()
    private val writeViewModel by viewModels<WriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setupTagRecyclerViewAdapter()
        setupRequireLaneRecyclerViewAdapter()
        setOnAddLaneButtonListener()
        setOnCreateRoomClickListener()
        registerTagViewModel()
        addTagsButton()
        registerViewModelEvent()
        setGameModeSpinner()
        checkBodyMaxLength()
        checkTitleMaxLength()
    }


    private fun checkBodyMaxLength() {
        binding.bodyEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val lines = it.split("\n")
                    if (lines.size > 3) {
                        binding.bodyEditTextView.removeTextChangedListener(this)
                        binding.bodyEditTextView.setText(it.subSequence(0, start))
                        binding.bodyEditTextView.setSelection(binding.bodyEditTextView.length())
                        binding.bodyEditTextView.addTextChangedListener(this)
                    }
                        val currentLength = it.length
                        val bodyMaxLength = 50
                        binding.bodyMaxLengthTextView.text = "$currentLength/$bodyMaxLength"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun checkTitleMaxLength(){
        binding.titleEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val currentLength = it.length
                    val titleMaxLength = 20
                    binding.titleMaxLengthTextView.text = "$currentLength/$titleMaxLength"
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setGameModeSpinner() {
        val gameModeSpinner = binding.gameModeSpinner
        val gameModeSpinnerAdapter = GameModeSpinnerAdapter(this, Mode.entries.toTypedArray())
        gameModeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameModeSpinner.adapter = gameModeSpinnerAdapter

        gameModeSpinner.dropDownVerticalOffset = 100

        val firstMode = mode

        // mode 값이 null이 아닌 경우 spinner의 선택된 항목으로 설정 전체에서 만들었을 때는 일반이 기본값
        firstMode.let {
            val position = Mode.entries.indexOf(it)
            gameModeSpinner.setSelection(position)
        }

        gameModeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                mode = Mode.entries.toTypedArray()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }



    private fun setupTagRecyclerViewAdapter() = with(binding.writeTagRecyclerView) {
        adapter = TagAdapter(tagViewModel.tags.toMutableList(), tagViewModel::removeTag).apply {
            tagRecyclerViewAdapter = this
        }
        layoutManager = FlexboxLayoutManager(context)
        if (itemDecorationCount == 0) {
            addItemDecoration(FlexboxItemDecoration)
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
    private fun addTagsButton() {
        binding.tagBackgroundCardView.setOnClickListenerWithCooldown(Interval.OPEN_SCREEN) {
            TagBottomSheetFragment.show(supportFragmentManager)
        }
    }

    //리싸이클러뷰 어댑터
    private fun setupRequireLaneRecyclerViewAdapter() {
        requireLaneRecyclerViewAdapter =
            RequireLaneRecyclerViewAdapter(requireLaneList, onLaneCountChanged = {
                binding.addLaneCardView.visibility =
                    if (requireLaneRecyclerViewAdapter.itemCount < 4) View.VISIBLE else View.INVISIBLE
            })
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
        val addLaneBtn = binding.addLaneCardView
        addLaneBtn.setOnClickListener {
            requireLaneRecyclerViewAdapter.addLane("1")
        }
    }

    //생성하기 버튼
    private fun setOnCreateRoomClickListener() {
        binding.createGroupButton.setOnClickListenerWithCooldown(Interval.CREATE_GROUP) {
            val hasDuplicateLanes = checkDuplicateLanes()
            val hasDefaultLane = checkDefaultLane()
            val title = binding.titleEditTextView.text.toString()

            if (title.isBlank()) {
                showSnackbar("제목이 입력되지 않았습니다")
                return@setOnClickListenerWithCooldown
            }
            if (hasDefaultLane) {
                showSnackbar("설정하지 않은 라인이 있습니다")
                return@setOnClickListenerWithCooldown
            }
            if (hasDuplicateLanes) {
                showSnackbar("중복된 라인이 존재합니다")
                return@setOnClickListenerWithCooldown
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
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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

    private fun registerViewModelEvent() {
        lifecycleScope.launch {
            writeViewModel.event.flowWithLifecycle(lifecycle).collectLatest { writeEvent ->
                when (writeEvent) {
                    is WriteEvent.Success -> {
                        showSnackbar("생성이 완료되었습니다")
                        setResult(ResultCode.REFRESH)
                        finish()
                    }

                    is WriteEvent.Notice -> showSnackbar(writeEvent.message)
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

}