package com.ashutosh.auth2.ui.auth

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashutosh.auth2.data.AuthRepository
import com.ashutosh.auth2.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class  AuthViewModel @Inject constructor(
    private val repository : AuthRepository
): ViewModel() {

    var username = ObservableField<String>("Ash")
    var pass = ObservableField<String>("")
    var email = ObservableField<String>("")

    private val _loginFlow  = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow : StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow  = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow : StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init{
        if(repository.currentUser!=null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email:String, password:String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }
    fun signup() = viewModelScope.launch {

        Log.d("signup","singup")
        Log.d("signup","sing up ${email.get().toString()}")
        _signupFlow.value = Resource.Loading
        val result = repository.signup(username.toString(), email.get().toString(), pass.get().toString())
        _signupFlow.value = result
        _isSignedIn.postValue(true)
    }

    fun logout(){
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }

    fun onSignupClicked(v : View) {
        signup()
    }
}