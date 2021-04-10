package ir.ah.app.runnerman.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.ah.app.runnerman.data.database.RunnerManDatabase
import ir.ah.app.runnerman.other.Constants.RUNNING_DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideRunningDatabase(@ApplicationContext
    context: Context)= Room.databaseBuilder(
        context,
        RunnerManDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(database: RunnerManDatabase)=database.getRunDao()

}