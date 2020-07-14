package lib.shishkin.sl.task

import lib.shishkin.sl.request.IRequest
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit


class DbExecutor : AbsRequestExecutor() {
    companion object {
        const val NAME = "DbExecutor"
    }

    private val QUEUE_CAPACITY = 1024
    private val threadCount = 4
    private val maxThreadCount = 4
    private val keepAliveTime: Long = 10 // 10 мин
    private val unit = TimeUnit.MINUTES
    private val queue = PriorityBlockingQueue<IRequest>(QUEUE_CAPACITY)
    private val executor = RequestThreadPoolExecutor(
        threadCount,
        maxThreadCount,
        keepAliveTime,
        unit,
        queue as BlockingQueue<Runnable>
    )

    override fun getExecutor(): RequestThreadPoolExecutor {
        return executor
    }

    override fun getName(): String {
        return NAME
    }
}
