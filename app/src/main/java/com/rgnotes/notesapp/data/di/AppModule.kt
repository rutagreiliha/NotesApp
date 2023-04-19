package com.rgnotes.notesapp.data.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rgnotes.notesapp.data.firebase.FirebaseAuthImplemented
import com.rgnotes.notesapp.data.firebase.FirebaseAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryAuthImplemented
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.viewmodel.AuthViewmodel
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
    fun provideRepositoryAuthImplemented(firebaseAuth: FirebaseAuthInterface): RepositoryAuthImplemented = RepositoryAuthImplemented(firebaseAuth)

    @Singleton
    @Provides
    fun provideAuthViewmodel(repository: RepositoryAuthInterface): AuthViewmodel = AuthViewmodel(repository)


}