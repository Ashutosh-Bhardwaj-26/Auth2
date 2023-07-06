package com.ashutosh.auth2.ui.auth.phone_auth

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashutosh.auth2.data.Resource
import com.ashutosh.auth2.data.utils.helper.SharedPreferenceHelper
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel()  {
    var auth = FirebaseAuth.getInstance()
    var phoneNumber = ObservableField<String>("")
    var otp = ObservableField<String>("")
    var activity : Activity ? = null
    private var isResendTextViewEnabled = false
    var storedVerificationId : String ?= null
    var resendToken : PhoneAuthProvider.ForceResendingToken ?= null

    private val _signInResult = MutableLiveData<Resource<PhoneAuthCredential>>()
    val signInResult: LiveData<Resource<PhoneAuthCredential>>
        get() = _signInResult

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn



    fun onSendOtpClick(activity : Activity){
        Log.d("otp","sent")
        this.activity = activity
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91${phoneNumber.get().toString()}") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("otp","done")
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("otp","invalid")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("otp","many")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d("otp","codesent")
            storedVerificationId = verificationId
            Log.d("otp",storedVerificationId.toString())

            var editor = sharedPreferences!!.edit()
            editor.putString("verification",storedVerificationId.toString())
            editor.apply()

            resendToken = token

        }
    }


    fun onOptEnter(){
        Log.d("otp","entered")
        Log.d("otp",otp.get().toString())
        val verificationId  = sharedPreferences.getString("verification",null)
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(verificationId.toString(), otp.get().toString())
        Log.d("otp cred", credential.toString())
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = it.result?.user
                    _signInResult.value = Resource.Success(credential)
                    _isSignedIn.postValue(true)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", it.exception)
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        _isSignedIn.postValue(false)
                        _signInResult.value = Resource.Failure(it.exception as FirebaseAuthInvalidCredentialsException)
                    }
                }
            }
    }

    fun checkIfUserLoggedIn(): Boolean {
        val user = firebaseAuth.currentUser
        Log.d("user",user.toString())
        return user != null
    }

    fun checkIfFirstAppOpened(): Boolean = true

    companion object {
        const val TAG = "PhoneAuthViewModel"
    }
}




