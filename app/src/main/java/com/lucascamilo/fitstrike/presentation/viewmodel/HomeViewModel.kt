package com.lucascamilo.fitstrike.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamilo.fitstrike.domain.usecase.GetUserNameUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserNameUseCase: GetUserNameUseCase
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun getUserName() {
        viewModelScope.launch {
            val name = getUserNameUseCase()
            _userName.value = name ?: "Usuário desconhecido"
        }
    }
}