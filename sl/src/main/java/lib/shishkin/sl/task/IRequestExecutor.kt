package lib.shishkin.sl.task

import lib.shishkin.sl.request.IRequest


interface IRequestExecutor : IExecutor {

    fun execute(request: IRequest)

    fun isShutdown(): Boolean

}
