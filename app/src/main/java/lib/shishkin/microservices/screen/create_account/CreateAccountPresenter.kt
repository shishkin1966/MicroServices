package lib.shishkin.microservices.screen.create_account

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.CreateAccountTransaction
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.sl.action.HideKeyboardAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.StopAction
import lib.shishkin.sl.model.AbsModelPresenter

class CreateAccountPresenter(model: CreateAccountModel) : AbsModelPresenter(model) {
    companion object {
        const val NAME = "CreateAccountPresenter"
    }

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is CreateAccountTransaction) {
            Providers.createAccount(action.account)
            getView<CreateAccountFragment>().addAction(HideKeyboardAction())
            getView<CreateAccountFragment>().addAction(StopAction())
            return true
        }

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

}
