package lib.shishkin.microservices.provider.notification

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import lib.shishkin.microservices.ApplicationSingleton


class ForegroundService : Service() {
    private val id: Int = 1;
    private var notification: Notification? = null;

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val message = intent.getStringExtra("message")
        val title = intent.getStringExtra("title")
        val provider =
            ApplicationSingleton.instance.get<INotificationProvider>(NotificationProvider.NAME)
        notification = provider?.getNotification(title, message!!)
        startForeground(id, notification)
        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}