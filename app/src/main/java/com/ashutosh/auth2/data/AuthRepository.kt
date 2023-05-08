package com.ashutosh.auth2.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email:String, password:String): Resource<FirebaseUser>
    suspend fun signup(name:String, email:String, password:String): Resource<FirebaseUser>
    fun logout()
}
