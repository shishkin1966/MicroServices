package lib.shishkin.sl.task

import lib.shishkin.sl.RefSecretary
import lib.shishkin.sl.provider.ErrorSingleton
import lib.shishkin.sl.request.IRequest
import lib.shishkin.sl.request.IResultMessageRequest
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class RequestThreadPoolExecutor(
    corePoolSize: Int,
    maximumPoolSize: Int,
    keepAliveTime: Long,
    unit: TimeUnit,
    workQueue: BlockingQueue<Runnable>
) : ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    unit,
    workQueue,
    JobThreadFactory()
), IExecutor {
    companion object {
        const val ACTION_NOTHING = -1
        const val ACTION_DELETE = 0
        const val ACTION_IGNORE = 1
    }

    private val requests = RefSecretary<IRequest>()

    fun addRequest(request: IRequest) {
        var action = ACTION_NOTHING
        if (request.isDistinct() && requests.containsKey(request.getName())) {
            val oldRequest = requests.get(request.getName())
            if (oldRequest != null) {
                action = request.getAction(oldRequest)
                if (action == ACTION_DELETE) {
                    oldRequest.setCanceled()
                }
            }
        }
        if (action != ACTION_IGNORE) {
            requests.put(request.getName(), request)
            execute(request)
        }
    }

    override fun afterExecute(r: Runnable, t: Throwable?) {
        super.afterExecute(r, t)

        if (t != null) {
            ErrorSingleton.instance.onError(javaClass.name, t)
        }
    }

    override fun beforeExecute(t: Thread, r: Runnable) {
        var priority = Thread.NORM_PRIORITY
        if (r is IRequest) {
            val rank = r.getRank()
            if (rank < 4) {
                priority = Thread.MIN_PRIORITY
            } else if (rank > 6) {
                priority = Thread.MAX_PRIORITY
            }
        }
        t.priority = priority
        super.beforeExecute(t, r)
    }

    override fun clear() {
        for (request in requests.values()) {
            request.setCanceled()
        }

        requests.clear()
    }

    override fun cancelRequests(listener: String) {
        for (request in requests.values()) {
            if (request is IResultMessageRequest) {
                if (request.isValid() && request.getOwner() == listener) {
                    request.setCanceled()
                }
            }
        }
    }

    override fun cancelRequests(listener: String, taskName: String) {
        for (request in requests.values()) {
            if (request is IResultMessageRequest) {
                if (request.isValid() && request.getOwner() == listener && taskName == request.getName()) {
                    request.setCanceled()
                }
            }
        }
    }

    override fun stop() {
        clear()
        shutdownNow()
    }


}
