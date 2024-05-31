package kr.cosine.groupfinder.presentation.view.write

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.model.SpinnerModel
import kr.cosine.groupfinder.databinding.ActivityWriteBinding
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.TagItemDecoration
import kr.cosine.groupfinder.presentation.view.write.adapter.RequireLaneRecyclerViewAdapter
import kr.cosine.groupfinder.presentation.view.write.adapter.SpinnerAdapter
import kr.cosine.groupfinder.presentation.view.write.adapter.TagRecyclerViewAdapter

class WriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBinding
    private lateinit var tagRecyclerViewAdapter: TagRecyclerViewAdapter
    lateinit var requireLaneRecyclerViewAdapter: RequireLaneRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initSpinner()
        setupTagRecyclerViewAdapter()
        setupRequireLaneRecyclerViewAdapter()
        setOnAddLaneButtonListener()

        val tagRecyclerView :RecyclerView = binding.writeTagRecyclerView
        tagRecyclerView.addItemDecoration(TagItemDecoration())


    }
    private fun setupTagRecyclerViewAdapter(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.writeTagRecyclerView.layoutManager = layoutManager
        tagRecyclerViewAdapter = TagRecyclerViewAdapter(dummytag)
        binding.writeTagRecyclerView.adapter = tagRecyclerViewAdapter
    }

    private fun setupRequireLaneRecyclerViewAdapter(){
        requireLaneRecyclerViewAdapter = RequireLaneRecyclerViewAdapter(mutableListOf("1"))
        binding.requireLanesRecyclerView.apply {
            adapter = requireLaneRecyclerViewAdapter
            itemAnimator = null
        }
    }
    private fun initSpinner(){
        val spinner = binding.selectMyLaneSpinner
        val items = LaneSpinnerItem.laneItems
        val adapter = SpinnerAdapter(this, items)
        spinner.adapter = adapter

    }

    //라인 더하는 기능
    private fun setOnAddLaneButtonListener(){
        binding.addLaneCardView.setOnClickListener {
            if (requireLaneRecyclerViewAdapter.itemCount >= 4){
                Toast.makeText(this, "더 이상 라인을 추가할 수 없습니다",Toast.LENGTH_SHORT).show()
            }
            else{
                requireLaneRecyclerViewAdapter.addLane("1")
            }
        }
    }


}