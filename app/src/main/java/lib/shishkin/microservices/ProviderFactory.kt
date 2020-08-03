package lib.shishkin.microservices

import lib.shishkin.microservices.provider.*
import lib.shishkin.microservices.provider.notification.NotificationProvider
import lib.shishkin.sl.INamed
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.IProviderFactory
import lib.shishkin.sl.provider.*
import lib.shishkin.sl.task.CommonExecutor
import lib.shishkin.sl.task.DbExecutor
import lib.shishkin.sl.task.NetExecutor

object ProviderFactorySingleton {
    val instance = ProviderFactory()
}

class ProviderFactory : IProviderFactory, INamed {
    companion object {
        const val NAME = "ProviderFactory"
    }

    override fun create(name: String): IProvider? {
        return try {
            when (name) {
                ErrorProvider.NAME -> ErrorSingleton.instance
                CrashProvider.NAME -> CrashProvider()
                ApplicationSingleton.instance.getName() -> ApplicationSingleton.instance
                ActivityUnion.NAME -> ActivityUnion()
                PresenterUnion.NAME -> PresenterUnion()
                MessengerUnion.NAME -> MessengerUnion()
                ObservableUnion.NAME -> ObservableUnion()
                CommonExecutor.NAME -> CommonExecutor()
                DbExecutor.NAME -> DbExecutor()
                DbProvider.NAME -> DbProvider()
                NotificationProvider.NAME -> NotificationProvider()
                LocationUnion.NAME -> LocationUnion()
                NetExecutor.NAME -> NetExecutor()
                NetProvider.NAME -> NetProvider()
                NetCbProvider.NAME -> NetCbProvider()
                NetImageProvider.NAME -> NetImageProvider()
                else -> Class.forName(name).newInstance() as IProvider
            }
        } catch (e: Exception) {
            ErrorSingleton.instance.onError(getName(), e)
            null
        }
    }

    override fun getName(): String {
        return NAME
    }

}
