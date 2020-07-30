package lib.shishkin.microservices.provider.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build


import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationConstant
import lib.shishkin.microservices.R
import lib.shishkin.microservices.screen.main.MainActivity
import lib.shishkin.sl.provider.ApplicationProvider

@RequiresApi(Build.VERSION_CODES.O)
class NotificationO : INotificationShortProvider {
    companion object {
        const val CANAL_NAME = "Notification Service Canal"
    }

    private val GROUP_NAME = ApplicationProvider.appContext.getString(R.string.app_name)
    private var id = -1
    private val nm: NotificationManager = ApplicationUtils.getSystemService(
        ApplicationProvider.appContext,
        Context.NOTIFICATION_SERVICE
    )

    override fun addNotification(title: String?, message: String) {
        id = System.currentTimeMillis().toInt()
        show(title, message)
    }

    override fun replaceNotification(title: String?, message: String) {
        if (id == -1) {
            id = System.currentTimeMillis().toInt()
        }
        show(title, message)
    }


    private fun show(title: String?, message: String) {
        val context = ApplicationProvider.appContext

        var channel: NotificationChannel? = nm.getNotificationChannel(GROUP_NAME)
        if (channel == null) {
            channel =
                NotificationChannel(GROUP_NAME, CANAL_NAME, NotificationManager.IMPORTANCE_LOW)
            channel.enableLights(true)
            channel.lightColor = R.color.red
            nm.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.action = ApplicationConstant.NOTIFICATION_CLICK

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder = NotificationCompat.Builder(context, GROUP_NAME)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.icon)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .setDefaults(0)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setGroup(GROUP_NAME)
            .setContentText(message)
        if (!title.isNullOrEmpty()) {
            notificationBuilder.setContentTitle(title)
        }
        nm.notify(id, notificationBuilder.build())
    }


    override fun clear() {
        nm.cancelAll()
    }

    override fun getId(): Int {
        return id
    }

    override fun setId(id: Int) {
        this.id = id
    }

}
