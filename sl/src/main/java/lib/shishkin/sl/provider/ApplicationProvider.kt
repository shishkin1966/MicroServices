package lib.shishkin.sl.provider

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.IServiceLocator


open class ApplicationProvider : MultiDexApplication(),
    IApplicationProvider {
    private var isExit = false

    companion object {
        val instance = ApplicationProvider()
        lateinit var appContext: Context
        var serviceLocator: IServiceLocator? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
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
        return ""
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
