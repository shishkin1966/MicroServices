package lib.shishkin.microservices.screen.net_json

import lib.shishkin.sl.model.AbsPresenterModel


class NetJsonModel(view: NetJsonFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(NetJsonPresenter(this))
    }
}
