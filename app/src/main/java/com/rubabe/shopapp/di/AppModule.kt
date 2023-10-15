package com.rubabe.shopapp.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
}