package lib.shishkin.sl.model

import lib.shishkin.sl.presenter.IModelPresenter


abstract class AbsPresenterModel(modelView: IModelView) : AbsModel(modelView), IPresenterModel {
    private lateinit var presenter: IModelPresenter

    override fun setPresenter(presenter: IModelPresenter) {
        this.presenter = presenter
        super.addStateObserver(this.presenter)
    }

    override fun <C> getPresenter(): C {
        return presenter as C
    }
}
