package kr.cosine.groupfinder.presentation

import android.os.Bundle
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.cosine.groupfinder.R

class WriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_write)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val spinner = findViewById<Spinner>(R.id.selectMyLaneSpinner)
        val items = listOf(
            SpinnerModel(R.drawable.ic_lane_top, "탑"),
            SpinnerModel(R.drawable.ic_lane_jungle, "정글"),
            SpinnerModel(R.drawable.ic_lane_mid, "미드"),
            SpinnerModel(R.drawable.ic_lane_ad, "원딜"),
            SpinnerModel(R.drawable.ic_lane_spt, "서포터")
        )
        val adapter = SpinnerAdapter(this, items)
        spinner.adapter = adapter
    }
}