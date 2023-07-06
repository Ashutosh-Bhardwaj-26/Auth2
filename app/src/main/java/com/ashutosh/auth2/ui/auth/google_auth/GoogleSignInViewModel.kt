package com.ashutosh.auth2.ui.auth.google_auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashutosh.auth2.data.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val googleSignInClient: GoogleSignInClient
) :ViewModel() {

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    private val _signInResult = MutableLiveData<Resource<GoogleSignInAccount>>()
    val signInResult: LiveData<Resource<GoogleSignInAccount>>
        get() = _signInResult

    fun signInWithGoogle(requestCode: Int, data: Intent?) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.d("1. ", " here")
                val account = task.getResult(ApiException::class.java)
                _isSignedIn.postValue(true)
                _signInResult.postValue(Resource.Success(account))
            } catch (e: ApiException) {
                _signInResult.postValue(Resource.Failure(e))
            }
        }
    }


    fun signOutFromGoogle() {
        viewModelScope.launch(Dispatchers.IO) {
            googleSignInClient.signOut().addOnCompleteListener {
                _isSignedIn.postValue(false)
            }
        }
    }

}