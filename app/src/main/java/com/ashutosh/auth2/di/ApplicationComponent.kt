package com.ashutosh.auth2.di

import com.ashutosh.auth2.ui.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

    fun inject(homeFragment: HomeFragment)
}