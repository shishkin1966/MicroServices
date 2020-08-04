package lib.shishkin.microservices

import lib.shishkin.microservices.provider.LocationUnion
import lib.shishkin.microservices.provider.NetCbProvider
import lib.shishkin.microservices.provider.NetImageProvider
import lib.shishkin.microservices.provider.NetProvider
import lib.shishkin.microservices.provider.notification.NotificationProvider
import lib.shishkin.sl.AbsServiceLocator
import lib.shishkin.sl.IProviderFactory
import lib.shishkin.sl.observe.NetObservable
import lib.shishkin.sl.observe.ObjectObservable
import lib.shishkin.sl.observe.ScreenBroadcastReceiverObservable
import lib.shishkin.sl.provider.CrashProvider
import lib.shishkin.sl.provider.ErrorSingleton
import lib.shishkin.sl.provider.IObservableUnion
import lib.shishkin.sl.provider.ObservableUnion
import lib.shishkin.sl.task.DbExecutor
import lib.shishkin.sl.task.NetExecutor
import lib.shishkin.sl.task.PicassoExecutor

object ServiceLocatorSingleton {
    val instance = ServiceLocator()
}

class ServiceLocator : AbsServiceLocator() {
    companion object {
        const val NAME = "ServiceLocator"
    }

    override fun getName(): String {
        return NAME
    }

    override fun start() {
        registerProvider(ErrorSingleton.instance)
        registerProvider(ApplicationSingleton.instance)
        registerProvider(CrashProvider.NAME)

        registerProvider(ObservableUnion.NAME)
        val union = get<IObservableUnion>(ObservableUnion.NAME)
        union?.register(NetObservable())
        union?.register(ScreenBroadcastReceiverObservable())
        union?.register(ObjectObservable())

        registerProvider(NotificationProvider.NAME)
        registerProvider(LocationUnion.NAME)

        registerProvider(NetExecutor.NAME)
        registerProvider(DbExecutor.NAME)

        registerProvider(NetProvider.NAME)
        registerProvider(NetCbProvider.NAME)
        registerProvider(NetImageProvider.NAME)
    }

    override fun getProviderFactory(): IProviderFactory {
        return ProviderFactorySingleton.instance
    }

}
