package lib.shishkin.microservices.screen.accounts

import lib.shishkin.sl.model.AbsPresenterModel


class AccountsModel(view: AccountsFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(AccountsPresenter(this))
    }
}
