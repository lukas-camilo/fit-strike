package com.lucascamilo.fitstrike.data.remote

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.amazonaws.services.cognitoidentityprovider.model.UsernameExistsException
import com.lucascamilo.fitstrike.domain.exception.ResendVerificationCodeRequiredException
import com.lucascamilo.fitstrike.domain.exception.VerificationCodeRequiredException

import com.lucascamilo.fitstrike.domain.model.Token

class CognitoService(
    context: Context
) {

    private val userPool = CognitoUserPool(
        context,
        "us-east-1_UdaK4jaHL",
        "6p8fic53fd4qdlaopskfvtcn34",
        null,
        Regions.US_EAST_1)

    fun login(
        username: String,
        password: String,
        onSuccess: (Token) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = userPool.getUser(username)
        user.getSessionInBackground(object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                if (userSession != null) {
                    val token = Token(
                        accessToken = userSession.accessToken.jwtToken,
                        refreshToken = userSession.refreshToken.token
                    )
                    onSuccess(token)
                } else {
                    onError(Exception("User session is null"))
                }
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                if (authenticationContinuation != null) {
                    authenticationContinuation.setAuthenticationDetails(
                        AuthenticationDetails(username, password, null)
                    )
                    authenticationContinuation.continueTask()
                } else {
                    onError(Exception("Authentication continuation is null"))
                }
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                // Implemente este método se o MFA estiver habilitado no Cognito
                onError(Exception("MFA is required but not implemented"))
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                // Implemente este método se houver desafios adicionais configurados no Cognito
                onError(Exception("Authentication challenge is required but not implemented"))
            }

            override fun onFailure(exception: Exception?) {
                if (exception is UserNotConfirmedException) {
                    // Reenviar o código de verificação
                    userPool.getUser(username).resendConfirmationCodeInBackground(object : VerificationHandler {
                        override fun onSuccess(verificationCodeDeliveryMedium: CognitoUserCodeDeliveryDetails?) {
                            // Código de verificação enviado com sucesso
                            onError(ResendVerificationCodeRequiredException(username))
                        }

                        override fun onFailure(exception: Exception?) {
                            // Falha ao enviar o código de verificação
                            onError(exception ?: Exception("Erro ao reenviar o código de verificação."))
                        }
                    })
                } else {
                    onError(exception ?: Exception("Unknown error"))
                }
            }
        })
    }

    fun refreshToken(refreshToken: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        val user = userPool.currentUser

        user.getSession(object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                userSession?.let {
                    onSuccess(it.accessToken.jwtToken)
                }
            }

            override fun onFailure(exception: Exception?) {
                onError(exception ?: Exception("Unknown error"))
            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {}
            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {}
            override fun authenticationChallenge(continuation: ChallengeContinuation?) {}
        })
    }

    fun registerUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userAttributes = com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes()
        userAttributes.addAttribute("email", email)

        userPool.signUpInBackground(
            email,
            password,
            userAttributes,
            null,
            object : SignUpHandler {
                override fun onSuccess(user: CognitoUser?, result: SignUpResult?) {
                    if (result != null && result.isUserConfirmed) {
                        onSuccess(result.userSub)
                    } else {
                        onError(VerificationCodeRequiredException(email))
                    }
                }

                override fun onFailure(exception: Exception?) {
                    if (exception is UsernameExistsException) {
                        // Email já cadastrado
                        onError(UsernameExistsException(email))
                    } else {
                        // Outros erros
                        onError(exception ?: Exception("Erro desconhecido."))
                    }
                }
            }
        )
    }

    fun confirmUser(
        email: String,
        confirmationCode: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = userPool.getUser(email)
        user.confirmSignUpInBackground(confirmationCode, false, object : GenericHandler {
            override fun onSuccess() {
                onSuccess()
            }

            override fun onFailure(exception: Exception) {
                onError(exception)
            }
        })
    }
}