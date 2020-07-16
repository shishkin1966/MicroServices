package lib.shishkin.sl.model

import lib.shishkin.sl.presenter.IModelPresenter


interface IPresenterModel : IModel {
    /**
     * Установить презентер модели
     *
     * @param presenter презентер
     */
    fun setPresenter(presenter: IModelPresenter)

    /**
     * Получить презентер модели
     *
     * @return презентер
     */
    fun <C> getPresenter(): C

}
