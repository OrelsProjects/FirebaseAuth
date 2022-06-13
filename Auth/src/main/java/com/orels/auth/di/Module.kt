package com.orels.auth.di

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.orels.auth.data.interactor.AuthInteractorImpl
import com.orels.auth.data.repository.AuthRepository
import com.orels.auth.domain.exception.AuthNotInitializedException
import com.orels.auth.domain.interactor.AuthInteractor.Companion.SHARED_PREFS_FILE
import com.orels.auth.domain.interactor.AuthInteractor.Companion.TOKEN_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @com.orels.auth.di.AuthRepository
    @Provides
    fun provideAuthRepository(): AuthRepository =
        AuthRepository()

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val prefs: SharedPreferences = context.getSharedPreferences(
            SHARED_PREFS_FILE,
            Context.MODE_PRIVATE
        )
        val tokenId = prefs.getString(TOKEN_ID, null) ?: throw AuthNotInitializedException()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(tokenId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }
}