package lib.shishkin.microservices.provider.notification


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationConstant
import lib.shishkin.microservices.R
import lib.shishkin.microservices.screen.main.MainActivity
import lib.shishkin.sl.provider.ApplicationProvider

class Notification() : INotificationShortProvider {
    private val GROUP_NAME = ApplicationProvider.appContext.getString(R.string.app_name)
    private var id = 1
    private val nm: NotificationManager = ApplicationUtils.getSystemService(
        ApplicationProvider.appContext,
        Context.NOTIFICATION_SERVICE
    )

    override fun addNotification(title: String?, message: String) {
        show(getNotification(title, message))
    }

    private fun show(notification : Notification) {
        nm.notify(id, notification);
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

    override fun getNotification(title: String?, message: String): Notification {
        val context = ApplicationProvider.appContext
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
            .setContentText(message)
        if (!title.isNullOrEmpty()) {
            notificationBuilder.setContentTitle(title)
        }
        return notificationBuilder.build()
    }
}
