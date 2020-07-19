package lib.shishkin.microservices.screen.main

import android.content.Intent
import lib.shishkin.microservices.ApplicationConstant
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.SnackBarAction
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.provider.ApplicationProvider

class MainPresenter(model: MainModel) : AbsModelPresenter(model) {
    companion object {
        const val NAME = "MainPresenter"

        const val IntentAction = "IntentAction"
        const val ClearIntentAction = "ClearIntentAction"
    }

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is DataAction<*>) {
            when (action.getName()) {
                IntentAction -> {
                    parseIntent(action.getData() as Intent)
                    return true
                }
            }
        }

        if (action is SnackBarAction) {
            if (action.getName() == ApplicationProvider.appContext.getString(R.string.exit)) {
                Providers.exitApplication()
            }
            return true
        }

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    private fun parseIntent(intent: Intent) {
        val router = ApplicationSingleton.instance.routerProvider
        when (intent.action) {
            "android.intent.action.MAIN" -> {
                router.showRootFragment()
            }
            ApplicationConstant.ACTION_CLICK -> {
                if (router.hasTopFragment()) {
                    router.switchToTopFragment()
                } else {
                    router.showRootFragment()
                }
            }
        }
        getView<MainActivity>().addAction(ApplicationAction(ClearIntentAction))
    }

}
