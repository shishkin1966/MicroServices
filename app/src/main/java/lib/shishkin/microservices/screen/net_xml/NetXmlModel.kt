package lib.shishkin.microservices.screen.net_xml

import lib.shishkin.sl.model.AbsPresenterModel


class NetXmlModel(view: NetXmlFragment) : AbsPresenterModel(view) {
    init {
        setPresenter(NetXmlPresenter(this))
    }
}
