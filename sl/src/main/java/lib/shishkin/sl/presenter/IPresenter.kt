package lib.shishkin.sl.presenter

import lib.shishkin.sl.action.IActionHandler
import lib.shishkin.sl.action.IActionListener
import lib.shishkin.sl.lifecycle.ILifecycleListener
import lib.shishkin.sl.provider.IMessengerSubscriber

interface IPresenter : ILifecycleListener, IActionListener, IActionHandler,
    IMessengerSubscriber {
    /**
     * Флаг - регистрировать презентер в объединении презентеров
     *
     * @return true - регистрировать (презентер - глобальный)
     */
    fun isRegister(): Boolean
}
