package kr.cosine.groupfinder.presentation.view.profile

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.databinding.ActivityPolicyBinding
import kr.cosine.groupfinder.presentation.view.common.GroupFinderActivity
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown

@AndroidEntryPoint
class PolicyActivity : GroupFinderActivity() {

    private val binding by lazy { ActivityPolicyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerCloseButton()
    }

    private fun registerCloseButton() {
        binding.closeImageButton.setOnClickListenerWithCooldown {
            finish()
        }
    }
}