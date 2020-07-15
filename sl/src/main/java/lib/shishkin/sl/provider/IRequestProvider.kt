package lib.shishkin.sl.provider

import lib.shishkin.sl.IProvider
import lib.shishkin.sl.request.IRequest


interface IRequestProvider : IProvider {
    /**
     * Выполнить запрос
     *
     * @param request запрос
     */
    fun request(request: IRequest)
}
