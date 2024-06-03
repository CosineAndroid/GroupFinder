package kr.cosine.groupfinder.presentation.view.write

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ActivityWriteBinding
import kr.cosine.groupfinder.presentation.view.list.adapter.SearchTagAdpater
import kr.cosine.groupfinder.presentation.view.list.event.TagEvent
import kr.cosine.groupfinder.presentation.view.list.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.search.SearchFragment
import kr.cosine.groupfinder.presentation.view.write.adapter.RequireLaneRecyclerViewAdapter
import kr.cosine.groupfinder.presentation.view.write.adapter.SpinnerAdapter

class WriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBinding
    private lateinit var tagRecyclerViewAdapter: SearchTagAdpater
    lateinit var requireLaneRecyclerViewAdapter: RequireLaneRecyclerViewAdapter
    private val requireLaneList = mutableListOf(RequireLane("0"))
    private val selectedMyLaneList = mutableListOf<String>()
    private val defaultLane = LaneSpinnerItem.laneItems[0].lane
    private val tagViewModel by viewModels<TagViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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
        addTags()
        writeTagRecyclerView()

//        val tagRecyclerView: RecyclerView = binding.writeTagRecyclerView
//        tagRecyclerView.addItemDecoration(TagItemDecoration())


    }

    private fun setupTagRecyclerViewAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.writeTagRecyclerView.layoutManager = layoutManager
        tagRecyclerViewAdapter = SearchTagAdpater { position, tag ->
            tagViewModel.removeTag(position, tag)
        }
        binding.writeTagRecyclerView.adapter = tagRecyclerViewAdapter
    }

    private fun writeTagRecyclerView() = with(binding.writeTagRecyclerView) {
        adapter = SearchTagAdpater(tagViewModel::removeTag).apply {
            tagRecyclerViewAdapter = this
            itemAnimator = null
        }
        addItemDecoration(kr.cosine.groupfinder.presentation.view.list.adapter.decoration.impl.GroupTagItemDecoration)
    }

    private fun registerTagViewModel() {
        lifecycleScope.launch {
            tagViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is TagEvent.SetTag -> tagRecyclerViewAdapter.setTags(event.tags)
                    is TagEvent.AddTag -> tagRecyclerViewAdapter.addTag(event.tag)
                    is TagEvent.RemoveTag -> tagRecyclerViewAdapter.removeTag(event.position)
                }
            }
        }
    }

    //태그 더하기
    private fun addTags() {
        binding.tagbackgroundCardView.setOnClickListener {
            val searchFragment = SearchFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.writeInSearchLayout, searchFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    //레이아웃을 하나 만들어서 재사용, 커스텀뷰 호출

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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMyLane = items[position].lane
                if (selectedMyLane != defaultLane) {
                    selectedMyLaneList.clear()
                    selectedMyLaneList.add(selectedMyLane)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }

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
            //ex)viewmodel.createRoom
            //뷰모델의 에러 라이브데이터를 만들어 놓고 text에 그렇게 하는게 좋다.
            val hasDuplicateLanes = checkDuplicateLanes()
            val hasDefaultLane = checkDefaultLane()
            if (!hasDuplicateLanes && !hasDefaultLane) {
                val requireLaneSelected = requireLaneRecyclerViewAdapter.getLanes().map { it.lane }
                val roomTitle = binding.titleTextView.text.toString()
                val roomDescription = binding.bodyTextTextView.text.toString()
                val tags = tagViewModel.tags
                val allSelectedLanes = (selectedMyLaneList + requireLaneSelected).map { it to null }
                Toast.makeText(this, "생성이 완료되었습니다", Toast.LENGTH_SHORT).show()
            } else if (hasDefaultLane) {
                Toast.makeText(this, "설정하지 않은 라인이 있습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "중복된 라인이 존재합니다", Toast.LENGTH_SHORT).show()
            }
            Log.d("checkD", "${checkDuplicateLanes()}")
        }
    }

    //중복된 라인 체크
    private fun checkDuplicateLanes(): Boolean {
        val requireLaneSelected = requireLaneRecyclerViewAdapter.getLanes().map { it.lane }
        val allLanes = (selectedMyLaneList + requireLaneSelected)
        val laneSet = allLanes.toMutableSet()
        Log.d("checkalllanes", allLanes.size.toString())
        Log.d("checkalllanesSize", laneSet.size.toString())
        return allLanes.size != laneSet.size
    }

    //라인선택 했는지 체크
    private fun checkDefaultLane(): Boolean {
        val allLanes =
            (selectedMyLaneList + requireLaneRecyclerViewAdapter.getLanes().map { it.lane })
        return allLanes.contains(defaultLane)
    }
}