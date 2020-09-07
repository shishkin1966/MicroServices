package lib.shishkin.microservices.provider

import android.content.Context
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.sl.AbsProvider
import lib.shishkin.sl.IProvider


class DesktopProvider : AbsProvider(), IDesktopProvider {
    companion object {
        const val NAME = "DesktopProvider"
    }

    private var desktop = "" // default desktop

    override fun getName(): String {
        return NAME
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is IDesktopProvider) 0 else 1
    }

    override fun getLayoutId(name: String, defaultId: Int): Int {
        return getResourceId(name, "layout", defaultId)
    }

    override fun getColorId(name: String, defaultId: Int): Int {
        return getResourceId(name, "color", defaultId)
    }

    override fun getStyleId(name: String, defaultId: Int): Int {
        return getResourceId(name, "style", defaultId)
    }

    override fun getMenuId(name: String, defaultId: Int): Int {
        return getResourceId(name, "menu", defaultId)
    }

    override fun getResourceId(name: String, type: String, defaultId: Int): Int {
        val context: Context = ApplicationSingleton.instance.applicationContext
        var resource: String = name
        if (desktop.isNotEmpty()) {
            resource += "_$desktop"
        }
        val resId: Int = ApplicationUtils.getResourceId(context, type, resource)
        if (resId != 0) {
            return resId
        }
        return defaultId
    }

    override fun setDesktopTheme(desktop: String) {
        this.desktop = desktop
    }

    override fun getDesktopTheme(): String {
        return desktop
    }
}