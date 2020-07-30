package lib.shishkin.microservices.screen.map

import lib.shishkin.sl.model.AbsPresenterModel

class MapModel(view: MapFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(MapPresenter(this))
    }
}
