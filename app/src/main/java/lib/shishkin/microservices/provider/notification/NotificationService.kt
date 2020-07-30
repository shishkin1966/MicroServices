package lib.shishkin.microservices.provider.notification

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import lib.shishkin.microservices.ApplicationSingleton


class NotificationService : Service() {
    private var notification: Notification? = null;

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val message = intent.getStringExtra("message")
        val title = intent.getStringExtra("title")
        val provider =
            ApplicationSingleton.instance.get<INotificationProvider>(NotificationProvider.NAME)
        notification = provider?.getNotification(title, message!!)
        startForeground(provider?.getId()!!, notification)
        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}