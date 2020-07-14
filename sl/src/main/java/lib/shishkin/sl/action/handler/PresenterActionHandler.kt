package lib.shishkin.sl.action.handler

import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.IActionHandler
import lib.shishkin.sl.message.IMessage
import lib.shishkin.sl.presenter.IPresenter

class PresenterActionHandler(private val presenter: IPresenter) : IActionHandler {

    override fun onAction(action: IAction): Boolean {
        if (!presenter.isValid()) return false

        if (action is IMessage) {
            action.read(presenter)
            return true
        }
        return false
    }

}
