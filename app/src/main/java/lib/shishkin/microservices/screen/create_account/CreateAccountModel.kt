package lib.shishkin.microservices.screen.create_account

import lib.shishkin.sl.model.AbsPresenterModel


class CreateAccountModel(view: CreateAccountFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(CreateAccountPresenter(this))
    }
}
