package ir.ah.app.runnerman.di

import android.app.*
import android.content.*
import androidx.core.app.*
import com.google.android.gms.location.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.components.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.android.scopes.*
import ir.ah.app.runnerman.R
import ir.ah.app.runnerman.other.*
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_CHANNEL_ID
import ir.ah.app.runnerman.ui.*


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {



    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
    )=FusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
            @ApplicationContext context: Context
    )=PendingIntent.getActivity(
            context,
            0,
            Intent(context,
            MainActivity::class.java).also {
                it.action= Constants.ACTION_SHOW_TRACKING_FRAGMENT
            },
            PendingIntent.FLAG_UPDATE_CURRENT
    )


    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
            @ApplicationContext context: Context,
            pendingIntent: PendingIntent
    )=NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)

}