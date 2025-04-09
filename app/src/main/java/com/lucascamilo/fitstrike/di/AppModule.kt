package com.lucascamilo.fitstrike.di

import com.lucascamilo.fitstrike.data.local.TokenStorage
import com.lucascamilo.fitstrike.data.remote.CognitoService
import com.lucascamilo.fitstrike.data.repository.AuthRepositoryImpl
import com.lucascamilo.fitstrike.data.repository.TokenRepositoryImpl
import com.lucascamilo.fitstrike.domain.repository.AuthRepository
import com.lucascamilo.fitstrike.domain.repository.TokenRepository
import com.lucascamilo.fitstrike.domain.usecase.ConfirmationCodeUseCase
import com.lucascamilo.fitstrike.domain.usecase.GetAccessTokenUseCase
import com.lucascamilo.fitstrike.domain.usecase.LoginUseCase
import com.lucascamilo.fitstrike.domain.usecase.RefreshTokenUseCase
import com.lucascamilo.fitstrike.domain.usecase.RegisterUseCase
import com.lucascamilo.fitstrike.domain.usecase.SaveTokenUseCase
import com.lucascamilo.fitstrike.presentation.viewmodel.AuthViewModel
import com.lucascamilo.fitstrike.presentation.viewmodel.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //Storage
    single<TokenStorage> { TokenStorage(androidContext()) }

    //Service
    single<CognitoService> { CognitoService(androidContext()) }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get()) }

    // Use Cases
    single { GetAccessTokenUseCase(get()) }
    single { LoginUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
    single { SaveTokenUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { ConfirmationCodeUseCase(get()) }

    // ViewModels
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
}