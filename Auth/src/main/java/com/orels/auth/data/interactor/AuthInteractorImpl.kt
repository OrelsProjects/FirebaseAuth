package com.orels.auth.data.interactor

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.orels.auth.data.repository.AuthRepository
import com.orels.auth.domain.exception.AuthNotInitializedException
import com.orels.auth.domain.exception.UsernamePasswordAuthException
import com.orels.auth.domain.interactor.AuthInteractor
import com.orels.auth.domain.interactor.AuthInteractor.Companion.SHARED_PREFS_FILE
import com.orels.auth.domain.interactor.AuthInteractor.Companion.TOKEN_ID
import com.orels.auth.domain.model.User
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject


class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthInteractor {

    override fun initialize(context: Context, idToken: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        try {
            editor.putString(TOKEN_ID, idToken)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        editor.apply()
    }

    override val user: User?
        get() = authRepository.user?.uid?.let { User(uid = it) }

    override fun isAuth(): Boolean = authRepository.isAuth()

    override suspend fun auth(
        email: String,
        password: String,
        isSaveCredentials: Boolean
    ): User? {
        try {
            val authResult = authRepository.auth(email, password).await()
            if (isSaveCredentials) authRepository.saveCredentials(email, password)
            return authResult.user?.uid?.let { User(uid = it) }
        } catch (exception: Exception) {
            throw(UsernamePasswordAuthException(exception))
        }
    }

    override suspend fun googleAuth(account: GoogleSignInAccount) {
        val result = account.idToken?.let { authRepository.googleAuth(it) }
            ?.await()
//            ?.getResult(TaskException::class.java)
        println(result)
    }

    override fun isValidCredentials(email: String, password: String): Boolean =
        email.isNotBlank() && password.isNotBlank()

    override val signInRequest: BeginSignInRequest
        get() = authRepository.signInRequest
}