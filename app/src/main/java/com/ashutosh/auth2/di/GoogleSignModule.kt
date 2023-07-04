package com.ashutosh.auth2.di

import android.app.Application
import android.provider.Settings.Global.getString
import android.util.Log
import com.ashutosh.auth2.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleSignInModule {

    @Provides
    fun provideGoogleSignInClient(application: Application): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("611967289199-4cp7aq4fi55s367u0ii8bv96kduv4q7e.apps.googleusercontent.com")
            .requestEmail()
            .build()
        Log.d("module","reach")
        return GoogleSignIn.getClient(application, gso)
    }
}