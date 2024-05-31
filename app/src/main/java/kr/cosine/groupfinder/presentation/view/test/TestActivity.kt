package kr.cosine.groupfinder.presentation.view.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.databinding.ActivityTestBinding
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.GroupFragment

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val groupFragment = GroupFragment(Mode.ARAM)
        supportFragmentManager.beginTransaction().add(binding.root.id, groupFragment).commit()
    }
}