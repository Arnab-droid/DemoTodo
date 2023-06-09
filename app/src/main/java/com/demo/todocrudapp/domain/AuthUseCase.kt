package com.demo.todocrudapp.domain

import androidx.activity.result.ActivityResult
import com.demo.todocrudapp.data.repositories.AuthRepository
import com.demo.todocrudapp.data.network.model.Response
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    val userAuthenticatedStatus get() =  authRepository.userAuthenticatedStatus
    suspend fun signUpUser(email: String, password: String) =
        authRepository.signUpUser(email, password)

    suspend fun signInUser(email: String, password: String) =
        authRepository.signInUser(email, password)

    suspend fun signInWithGoogle(result: ActivityResult) =
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            authRepository.signInWithGoogle(account)
        } catch (e: Exception) {
            Response.Error(e)
        }
}