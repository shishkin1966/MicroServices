package lib.shishkin.microservices.screen.image

import lib.shishkin.sl.model.AbsPresenterModel


class NetImageModel(view: NetImageFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(NetImagePresenter(this))
    }
}
