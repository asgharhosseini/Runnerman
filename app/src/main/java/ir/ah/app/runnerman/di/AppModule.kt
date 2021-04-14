package ir.ah.app.runnerman.di

import android.content.*
import android.content.Context.*
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

import dagger.hilt.android.qualifiers.ApplicationContext
import ir.ah.app.runnerman.data.database.RunnerManDatabase
import ir.ah.app.runnerman.other.Constants.KEY_FIRST_TIME_TOGGLE
import ir.ah.app.runnerman.other.Constants.KEY_NAME
import ir.ah.app.runnerman.other.Constants.KEY_WEIGHT
import ir.ah.app.runnerman.other.Constants.RUNNING_DATABASE_NAME
import ir.ah.app.runnerman.other.Constants.SHARED_PREFERENCES_NAME
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
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

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
            app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) =
            sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)

}