package kr.cosine.groupfinder.presentation.view.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ActivityPolicyBinding

class PolicyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPolicyBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        close()
    }

    private fun close() {
        binding.closeImageButton.setOnClickListener {
            super.finish()
        }
    }
}