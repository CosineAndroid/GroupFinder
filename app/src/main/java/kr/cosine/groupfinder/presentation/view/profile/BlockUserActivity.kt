package kr.cosine.groupfinder.presentation.view.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.databinding.ActivityBlockUserBinding
import kr.cosine.groupfinder.presentation.view.common.extension.applyWhite
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.profile.adapter.BlockUserAdapter

@AndroidEntryPoint
class BlockUserActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBlockUserBinding.inflate(layoutInflater) }

    private lateinit var blockUserAdapter: BlockUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerProgressBar()
        registerCloseButton()
        registerRecyclerView()
        registerViewModelEvent()
    }

    private fun registerProgressBar() {
        binding.progressBar.applyWhite()
    }

    private fun registerCloseButton() {
        binding.closeImageButton.setOnClickListenerWithCooldown {
            finish()
        }
    }

    private fun registerRecyclerView() = with(binding.blockUserRecyclerView) {
        adapter = BlockUserAdapter {

        }.apply { blockUserAdapter = this }
    }

    private fun registerViewModelEvent() {

    }
}