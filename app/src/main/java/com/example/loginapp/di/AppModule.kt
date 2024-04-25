package com.example.loginapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.loginapp.data.LoginAppDb
import com.example.loginapp.data.dao.UserDao
import com.example.loginapp.data.mockdata.MockData
import com.example.loginapp.data.repositories.UserRepositoryImpl
import com.example.loginapp.domain.repositories.UserRepository
import com.example.loginapp.domain.use_cases.GetUsers
import com.example.loginapp.domain.use_cases.Login
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Volatile
    private var INSTANCE: LoginAppDb? = null

    @Provides
    @Singleton
    fun providesDb(
       @ApplicationContext context: Context
    ): LoginAppDb {
        return INSTANCE ?: synchronized(this) {
            val instance = INSTANCE
            if (instance != null) {
                instance
            } else {
                var callback = object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val db = INSTANCE ?: return@launch
                                val userDao = db.userDao()
                                populateDatabase(userDao)
                            }

                        }

                        private suspend fun populateDatabase(userDao: UserDao) {
                            val user = MockData.users
                            userDao.insertUser(user)
                        }

                }
                Room.databaseBuilder(context, LoginAppDb::class.java, "loginapp-db")
                    .addCallback(callback)
                    .build().also {
                        INSTANCE = it
                        }
            }
        }
    }


    @Provides
    @Singleton
    fun providesUserRepository(
        db: LoginAppDb
    ): UserRepository {
        return UserRepositoryImpl(db.userDao())
    }

    @Provides
    @Singleton
    fun providesLoginUseCase(
        userRepository: UserRepository
    ): Login {
        return Login(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(
        userRepository: UserRepository
    ): GetUsers {
        return GetUsers(userRepository)
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("loginApp", Context.MODE_PRIVATE)
    }

}