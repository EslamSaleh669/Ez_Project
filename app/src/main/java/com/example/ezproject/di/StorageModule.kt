package com.example.ezproject.di

import android.content.Context
import android.content.SharedPreferences
import com.example.ezproject.util.Constants
import dagger.Module
import dagger.Provides

@Module
class StorageModule(val context: Context) {

    @Provides
    fun provideSharedPreference(): SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE)
}