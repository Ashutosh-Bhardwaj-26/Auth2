package com.ashutosh.auth2.data

import android.util.Log
import com.ashutosh.auth2.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private var firebaseAuth: FirebaseAuth,
) : AuthRepository{
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser


    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(result.user!!)
        }catch (e : Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try{
            Log.d("repo","repo")
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        }catch (e : Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
    override fun logout() {
        firebaseAuth.signOut()
    }
}