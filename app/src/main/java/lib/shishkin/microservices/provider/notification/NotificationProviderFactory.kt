package lib.shishkin.microservices.provider.notification

import lib.shishkin.common.ApplicationUtils

object NotificationProviderFactory {

    fun get(): INotificationShortProvider {
        if (ApplicationUtils.hasO()) {
            return NotificationO()
        } else if (ApplicationUtils.hasMarshmallow()) {
            return NotificationM()
        }
        return Notification()
    }
}
