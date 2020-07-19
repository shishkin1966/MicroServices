package lib.shishkin.microservices

import android.widget.Toast
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.db.*
import lib.shishkin.microservices.observe.ScreenObservableSubscriber
import lib.shishkin.microservices.provider.DbProvider
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.IProviderSubscriber
import lib.shishkin.sl.message.IMessage
import lib.shishkin.sl.observe.ObjectObservable
import lib.shishkin.sl.presenter.IPresenter
import lib.shishkin.sl.provider.*
import lib.shishkin.sl.request.IRequest
import lib.shishkin.sl.task.CommonExecutor
import lib.shishkin.sl.task.DbExecutor
import lib.shishkin.sl.ui.IActivity

object ApplicationSingleton {
    val instance = App()
}

class App : ApplicationProvider() {

    private val screenObservableSubscriber = ScreenObservableSubscriber()

    override fun onCreate() {
        super.onCreate()

        ServiceLocatorSingleton.instance.start()

        serviceLocator = ServiceLocatorSingleton.instance

        ServiceLocatorSingleton.instance.registerSubscriber(screenObservableSubscriber)
    }

    fun onScreenOn() {
        ApplicationUtils.showToast(
            appContext,
            "Screen on",
            Toast.LENGTH_SHORT,
            ApplicationUtils.MESSAGE_TYPE_INFO
        )
    }

    fun onScreenOff() {
    }

    fun <C : IProvider> get(name: String): C? {
        return serviceLocator?.get(name)
    }

    fun onError(source: String, message: String?, isDisplay: Boolean) {
        val union = serviceLocator?.get<IErrorProvider>(ErrorProvider.NAME)
        union?.onError(source, message, isDisplay)
    }

    fun onError(source: String, e: Exception) {
        val union = serviceLocator?.get<IErrorProvider>(ErrorProvider.NAME)
        union?.onError(source, e)
    }

    fun <C : IPresenter> getPresenter(name: String): C? {
        val union = serviceLocator?.get<IPresenterUnion>(PresenterUnion.NAME)
        return union?.getPresenter(name)
    }

    var observableProvider: IObservableUnion
        get() = get(ObservableUnion.NAME)!!
        private set(value) {}

    var activityProvider: IActivityUnion
        get() = get(ActivityUnion.NAME)!!
        private set(value) {}

    var dbProvider: IDbProvider
        get() = get(DbProvider.NAME)!!
        private set(value) {}

    fun getDao(): Dao {
        return dbProvider.getDb<Db>()!!.getDao()
    }

    fun cancelRequests(name: String) {
        get<CommonExecutor>(CommonExecutor.NAME)!!.cancelRequests(name)
    }

    fun execute(request: IRequest) {
        get<CommonExecutor>(CommonExecutor.NAME)!!.execute(request)
    }

    fun executeDb(request: IRequest) {
        get<DbExecutor>(DbExecutor.NAME)!!.execute(request)
    }

    var routerProvider: IRouterProvider
        get() = activityProvider.getActivity<IActivity>() as IRouterProvider
        private set(value) {}

    fun addNotMandatoryMessage(message: IMessage) {
        get<IMessengerUnion>(MessengerUnion.NAME)!!.addNotMandatoryMessage(message)
    }

    fun registerSubscriber(subscriber: IProviderSubscriber): Boolean {
        return serviceLocator!!.registerSubscriber(subscriber)
    }

    fun unregisterSubscriber(subscriber: IProviderSubscriber): Boolean {
        return serviceLocator!!.unregisterSubscriber(subscriber)
    }

    fun onChange(observable: String, obj: Any) {
        observableProvider.getObservable(observable)?.onChange(obj)
    }

    fun onChange(name: String) {
        observableProvider.getObservable(ObjectObservable.NAME)?.onChange(name)
    }

    override fun getName(): String {
        return BuildConfig.APPLICATION_ID
    }

}
