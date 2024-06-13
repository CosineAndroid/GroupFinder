package kr.cosine.groupfinder.presentation.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.cosine.groupfinder.databinding.TaggedNicknameInputDialogBinding
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown

class TaggedNicknameInputDialog(
    private val nickname: String,
    private val tag: String,
    private val onConfirmClick: (String, String) -> Unit
) : DialogFragment() {

    private var _binding: TaggedNicknameInputDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaggedNicknameInputDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeWhiteBackground()
        registerNicknameEditText()
        registerTagEditText()
        registerCancelButton()
        registerConfirmButton()
    }

    private fun removeWhiteBackground() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun registerNicknameEditText() {
        binding.nicknameEditText.setText(nickname)
    }

    private fun registerTagEditText() {
        binding.tagEditText.setText(tag)
    }

    private fun registerCancelButton() {
        binding.cancelButton.setOnClickListenerWithCooldown(Interval.CLICK_BUTTON) {
            dismiss()
        }
    }

    private fun registerConfirmButton() = with(binding) {
        confirmButton.setOnClickListenerWithCooldown(Interval.CLICK_BUTTON) {
            onConfirmClick(nicknameEditText.text.toString(), tagEditText.text.toString())
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "TaggedNicknameInputDialog"
    }
}