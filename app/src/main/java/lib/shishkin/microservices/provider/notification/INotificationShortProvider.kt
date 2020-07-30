package lib.shishkin.microservices.provider.notification

import android.app.Notification

interface INotificationShortProvider {

    fun addNotification(title: String? = null, message: String)

    fun clear()

    fun getId(): Int

    fun setId(id: Int)

    fun getNotification(title: String? = null, message: String): Notification
}
