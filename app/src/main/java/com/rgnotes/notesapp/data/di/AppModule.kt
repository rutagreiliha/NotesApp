package com.rgnotes.notesapp.data.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rgnotes.notesapp.data.firebase.FirebaseAuthImplemented
import com.rgnotes.notesapp.data.firebase.FirebaseAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryAuthImplemented
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryDataImplemented
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.viewmodel.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataRepo(): RepositoryDataInterface = RepositoryDataImplemented()
    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuthInterface = FirebaseAuthImplemented()

    @Singleton
    @Provides
    fun provideRepositoryAuthImplemented(firebaseAuth: FirebaseAuthInterface): RepositoryAuthInterface = RepositoryAuthImplemented(firebaseAuth)



}