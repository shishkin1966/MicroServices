package lib.shishkin.sl.provider

import android.app.Application
import android.content.Context
import lib.shishkin.sl.BuildConfig
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.IServiceLocator

open class ApplicationProvider : Application(),
    IApplicationProvider {
    private var isExit = false

    companion object {
        val instance = ApplicationProvider()
        lateinit var appContext: Context
        var serviceLocator: IServiceLocator? = null
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }

    override fun isPersistent(): Boolean {
        return true
    }

    override fun onUnRegister() {
    }

    override fun onRegister() {
    }

    override fun stop() {
        isExit = true

        if (serviceLocator != null) {
            toBackground()
            serviceLocator?.stop()
        }
    }

    override fun toBackground() {
        val union = serviceLocator?.get<ActivityUnion>(
            ActivityUnion.NAME
        )
        union!!.getRouter()?.toBackground()
    }

    override fun getName(): String {
        return BuildConfig.APPLICATION_ID
    }

    override fun isValid(): Boolean {
        return !isExit
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is IApplicationProvider) 0 else 1
    }

    override fun isExit(): Boolean {
        return isExit
    }

}
