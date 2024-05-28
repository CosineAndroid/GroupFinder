package kr.cosine.groupfinder.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.cosine.groupfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}