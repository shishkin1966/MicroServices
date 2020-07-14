package lib.shishkin.sl.action.handler

import lib.shishkin.common.ApplicationUtils
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.IActionHandler
import lib.shishkin.sl.action.ShowMessageAction
import lib.shishkin.sl.provider.ApplicationProvider

open class BaseActionHandler : IActionHandler {
    override fun onAction(action: IAction): Boolean {
        if (action is ShowMessageAction) {
            showMessage(action)
            return true
        }

        return false
    }

    private fun showMessage(action: ShowMessageAction) {
        if (action.getMessage().isNullOrEmpty()) return

        ApplicationUtils.showToast(
            ApplicationProvider.appContext,
            action.getMessage(),
            action.getDuration(),
            action.getType()
        )
    }
}
