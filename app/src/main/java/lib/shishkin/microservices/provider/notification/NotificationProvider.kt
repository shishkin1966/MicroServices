package lib.shishkin.microservices.provider.notification

import android.app.Notification
import lib.shishkin.microservices.PreferencesUtils
import lib.shishkin.sl.AbsProvider
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.provider.ApplicationProvider

class NotificationProvider : AbsProvider(), INotificationProvider {
    companion object {
        const val NAME = "NotificationProvider"
    }

    private val provider: INotificationShortProvider = NotificationProviderFactory.get()

    override fun getName(): String {
        return NAME
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is INotificationProvider) 0 else 1
    }

    override fun addNotification(title: String?, message: String) {
        provider.addNotification(title, message)
    }

    override fun clear() {
        provider.clear()
    }

    override fun stop() {
        provider.clear()
    }

    override fun onUnRegister() {
        PreferencesUtils.putInt(ApplicationProvider.appContext, NAME, getId())

        super.onUnRegister()
    }

    override fun onRegister() {
        super.onRegister()

        setId(PreferencesUtils.getInt(ApplicationProvider.appContext, NAME))
    }

    override fun getId(): Int {
        return provider.getId()
    }

    override fun setId(id: Int) {
        provider.setId(id)
    }

    override fun getNotification(title: String?, message: String): Notification {
        return provider.getNotification(title, message)
    }


}
