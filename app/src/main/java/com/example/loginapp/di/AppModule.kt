package com.example.loginapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.loginapp.data.LoginDb
import com.example.loginapp.data.mockdata.MockData
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

    @Provides
    @Singleton
    fun provideSharedPreferences( @ApplicationContext context : Context) : SharedPreferences{
        return context.getSharedPreferences("loginApp",Context.MODE_PRIVATE)
    }

    @Volatile
    private var INSTANCE:LoginDb? = null

    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context : Context
    ) : LoginDb{
        return INSTANCE ?: synchronized(this){
            val instance = INSTANCE
            if(instance != null){
                return instance
            }
            else{
                var callback = object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = INSTANCE ?: return@launch
                            val userDao = db.userDao()
                            userDao.insertUsers(MockData.users)
                        }
                    }
                }
                return Room.databaseBuilder(
                    context,
                    LoginDb::class.java,
                    "login_db"
                )
                    .addCallback(callback)
                    .build()
            }
        }
    }

}