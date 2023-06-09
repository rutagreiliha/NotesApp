package com.rgnotes.notesapp.data.di

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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDataRepo(): RepositoryDataInterface = RepositoryDataImplemented()

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuthInterface = FirebaseAuthImplemented()

    @Singleton
    @Provides
    fun provideRepositoryAuthImplemented(firebaseAuth: FirebaseAuthInterface): RepositoryAuthInterface =
        RepositoryAuthImplemented(firebaseAuth)

}