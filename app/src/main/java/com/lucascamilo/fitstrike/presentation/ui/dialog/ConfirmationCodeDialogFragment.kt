package com.lucascamilo.fitstrike.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lucascamilo.fitstrike.R
import com.lucascamilo.fitstrike.databinding.FragmentConfirmationCodeDialogBinding

class ConfirmationCodeDialogFragment(
    private val onConfirm: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentConfirmationCodeDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmationCodeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar o botão de enviar código
        binding.submitButton.setOnClickListener {
            val code = binding.verificationCodeInput.text.toString()
            onConfirm(code)
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}