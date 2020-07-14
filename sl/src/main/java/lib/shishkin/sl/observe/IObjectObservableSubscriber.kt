package lib.shishkin.sl.observe

import lib.shishkin.sl.provider.IObservableSubscriber


interface IObjectObservableSubscriber : IObservableSubscriber {
    /**
     * Получить список слушаемых объектов БД
     *
     * @return список слушаемых объектов БД
     */
    fun getListenObjects(): List<String>
}
