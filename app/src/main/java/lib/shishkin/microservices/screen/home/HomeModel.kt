package lib.shishkin.microservices.screen.home

import lib.shishkin.sl.model.AbsPresenterModel


class HomeModel(view: HomeFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(HomePresenter(this))
    }
}
