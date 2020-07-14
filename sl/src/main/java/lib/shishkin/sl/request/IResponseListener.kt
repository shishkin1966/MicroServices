package lib.shishkin.sl.request

import lib.shishkin.sl.ISubscriber
import lib.shishkin.sl.data.ExtResult

interface IResponseListener : ISubscriber {

    /**
     * Событие - пришел ответ с результатами запроса
     *
     * @param result - результат
     */
    fun response(result: ExtResult)

}
