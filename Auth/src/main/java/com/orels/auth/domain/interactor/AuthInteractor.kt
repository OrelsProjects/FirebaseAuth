package com.orels.auth.domain.interactor

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.orels.auth.domain.model.User
interface AuthInteractor {

    companion object {
        const val SHARED_PREFS_FILE = "Auth_Share_File_qmi38em2#!loqwe##$#2aso,"
        const val TOKEN_ID = "Token_Id_as23±@ASD#!loqwe##$#2aso,"
    }

    fun initialize(context: Context, idToken: String)

    /**
     * The authenticated user.
     * @author Orel Zilberman, 19.11.2021
     */
    val user: User?

    /**
     * Used for google authentication.
     * @author Orel Zilberman, 28.4.2021
     */
    val signInRequest: BeginSignInRequest

    /**
     * Checks if there is an authenticated user.
     * @return a single with true if authenticated and false otherwise.
     * @author Orel Zilberman, 19.11.2021
     */
    fun isAuth(): Boolean

    /**
     * Authenticates a user by [email] and [password].
     * @author Orel Zilberman, 19.11.2021.
     */
    suspend fun auth(
        email: String,
        password: String,
        isSaveCredentials: Boolean = false
    ): User?

    /**
     * Authenticates a user with his mail.
     * @author Orel Zilberman, 15.4.2021
     */
//    suspend fun loginWithGmail(): FirebaseUser

    /**
     * Saves a gmail sign in session.
     * @author Orel Zilberman, 19.11.2021.
     */
    suspend fun googleAuth(account: GoogleSignInAccount)

    /**
     * Checks if the credentials entered are valid
     * according to the policy. TODO
     * @author Orel Zilberman, 19.11.2021
     */
    fun isValidCredentials(email: String, password: String): Boolean
}