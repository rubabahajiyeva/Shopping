package com.rubabe.shopapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.data.repo.ProductsDaoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideProductsDaoRepository(refProducts: DatabaseReference): ProductsDaoRepository{
        return ProductsDaoRepository(refProducts)
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(): DatabaseReference{
        val db = FirebaseDatabase.getInstance()
        return db.getReference("products")
    }


    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun providesFirebaseFirestore() = Firebase.firestore
}