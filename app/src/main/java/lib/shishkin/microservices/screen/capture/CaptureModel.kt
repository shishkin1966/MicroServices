package lib.shishkin.microservices.screen.capture

import lib.shishkin.sl.model.AbsPresenterModel


class CaptureModel(view: CaptureFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(CapturePresenter(this))
    }
}
