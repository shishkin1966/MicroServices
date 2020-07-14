package lib.shishkin.sl.provider

import lib.shishkin.sl.ISmallUnion
import lib.shishkin.sl.presenter.IPresenter

interface IPresenterUnion : ISmallUnion<IPresenter> {
    /**
     * Получить presenter
     *
     * @param name имя презентера
     * @return презентер
     */
    fun <C : IPresenter> getPresenter(name: String): C?

}
