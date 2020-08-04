package lib.shishkin.microservices.screen.image

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.action.ImageAction
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.model.AbsModelPresenter

class NetImagePresenter(model: NetImageModel) : AbsModelPresenter(model) {
    companion object {
        const val NAME = "NetImagePresenter"
    }

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is ImageAction) {
            Providers.getImage(action)
            return true
        }

        if (action is ApplicationAction) {
            when (action.getName()) {
                Actions.OnSwipeRefresh -> {
                    return true
                }
            }
        }

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

}
