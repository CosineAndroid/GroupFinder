package kr.cosine.groupfinder.presentation.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.cosine.groupfinder.databinding.SearchTestBinding

class SearchTestActivity: AppCompatActivity() {

    private val binding by lazy { SearchTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val searchFragment = SearchFragment()
        supportFragmentManager.beginTransaction().add(binding.root.id, searchFragment).commit()
    }
}