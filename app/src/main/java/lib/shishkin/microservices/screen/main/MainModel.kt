package lib.shishkin.microservices.screen.main

import lib.shishkin.sl.model.AbsPresenterModel


class MainModel(view: MainActivity) : AbsPresenterModel(view) {
    init {
        setPresenter(MainPresenter(this))
    }
}
