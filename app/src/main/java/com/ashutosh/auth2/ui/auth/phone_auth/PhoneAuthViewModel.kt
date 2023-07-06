package com.ashutosh.auth2.ui.auth.phone_auth

import android.app.Activity
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashutosh.auth2.data.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor() : ViewModel()  {
    var auth = FirebaseAuth.getInstance()
    var phoneNumber = ObservableField<String>("")
    var otp = ObservableField<String>("")
    var activity : Activity ? = null
    private var isResendTextViewEnabled = false
    var storedVerificationId : String ? = null
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
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            storedVerificationId = verificationId
            resendToken = token
        }
    }


    fun onOptEnter(){
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(storedVerificationId!!, otp.get().toString())
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    _signInResult.value = Resource.Success(credential)
                    _isSignedIn.postValue(true)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        _isSignedIn.postValue(false)
                        _signInResult.value = Resource.Failure(task.exception as FirebaseAuthInvalidCredentialsException)
                    }
                }
            }
    }

    companion object {
        const val TAG = "PhoneAuthViewModel"
    }
}




