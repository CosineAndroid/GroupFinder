package kr.cosine.groupfinder.presentation.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import kr.cosine.groupfinder.databinding.DialogBinding
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown

class Dialog(
    private val isCancelable: Boolean = true,
    private val title: String = "",
    private val message: String = "",
    private val cancelButtonTitle: String = "",
    private val confirmButtonTitle: String = "",
    private val cancelButtonVisibility: Int = View.VISIBLE,
    private val confirmButtonVisibility: Int = View.VISIBLE,
    private val onCancelClick: () -> Unit = {},
    private val onConfirmClick: () -> Unit = {}
) : DialogFragment() {

    private var _binding: DialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeWhiteBackground()
        registerTitleTextView()
        registerMessageTextView()
        registerCancelButton()
        registerConfirmButton()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(isCancelable)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.setOnKeyListener{_, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    !isCancelable
                } else {
                    false
                }
            }
        }
    }

    private fun removeWhiteBackground() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun registerTitleTextView() {
        binding.titleTextView.apply {
            if (title.isBlank()) {
                visibility = View.GONE
                return
            }
            text = title
        }
    }

    private fun registerMessageTextView() {
        binding.messageTextView.apply {
            text = message
        }
    }

    private fun registerCancelButton() {
        binding.cancelButton.apply {
            visibility = cancelButtonVisibility
            if (cancelButtonTitle.isNotBlank()) {
                text = cancelButtonTitle
            }
        }.setOnClickListenerWithCooldown(Interval.CLICK_BUTTON) {
            onCancelClick()
            dismiss()
        }
    }

    private fun registerConfirmButton() {
        binding.confirmButton.apply {
            visibility = confirmButtonVisibility
            if (confirmButtonTitle.isNotBlank()) {
                text = confirmButtonTitle
            }
        }.setOnClickListenerWithCooldown(Interval.CLICK_BUTTON) {
            onConfirmClick()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "GroupFinderDialog"

        fun dismiss(fragmentActivity: FragmentActivity) {
            (fragmentActivity.supportFragmentManager.findFragmentByTag(TAG) as? Dialog)?.dismiss()
        }
    }
}