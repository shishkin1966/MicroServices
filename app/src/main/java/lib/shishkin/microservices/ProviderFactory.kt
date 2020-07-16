package lib.shishkin.microservices

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
                ApplicationSingleton.instance.getName() -> ApplicationSingleton.instance
                ActivityUnion.NAME -> ActivityUnion()
                PresenterUnion.NAME -> PresenterUnion()
                MessengerUnion.NAME -> MessengerUnion()
                ObservableUnion.NAME -> ObservableUnion()
                CommonExecutor.NAME -> CommonExecutor()
                DbExecutor.NAME -> DbExecutor()
                NetExecutor.NAME -> NetExecutor()
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
