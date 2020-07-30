package lib.shishkin.microservices.screen.more

import lib.shishkin.sl.model.AbsPresenterModel

class MoreModel(view: MoreFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(MorePresenter(this))
    }
}
