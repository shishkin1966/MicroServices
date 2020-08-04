package lib.shishkin.microservices.provider

import com.squareup.picasso.*
import lib.shishkin.common.ApplicationUtils.Companion.runOnUiThread
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.ImageAction
import lib.shishkin.sl.AbsProvider
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.task.NetExecutor
import lib.shishkin.sl.task.PicassoExecutor
import java.lang.Exception
import java.util.concurrent.ExecutorService


class NetImageProvider : AbsProvider(), INetImageProvider {
    private var picasso: Picasso? = null

    companion object {
        const val NAME = "NetImageProvider"
    }

    override fun downloadImage(action: ImageAction) {
        if (picasso == null) {
            val executor = ApplicationSingleton.instance.get<PicassoExecutor>(PicassoExecutor.NAME)
            if (executor is ExecutorService) {
                picasso = Picasso.Builder(ApplicationProvider.appContext).executor(executor).build()
            } else {
                picasso = Picasso.Builder(ApplicationProvider.appContext).build()
            }
        }
        runOnUiThread(Runnable {
            if (!action.isWithCache()) {
                val requestCreator: RequestCreator = picasso!!.load(action.getUrl())
                if (action.getPlaceHolder() > 0) {
                    requestCreator.placeholder(action.getPlaceHolder())
                }
                if (action.getErrorHolder() > 0) {
                    requestCreator.error(action.getErrorHolder())
                }
                requestCreator.into(action.getView(), object : Callback {
                    override fun onSuccess() {}
                    override fun onError(exception: Exception) {}
                })
            } else {
                val requestCreator: RequestCreator = picasso!!.load(action.getUrl())
                if (action.getPlaceHolder() > 0) {
                    requestCreator.placeholder(action.getPlaceHolder())
                }
                if (action.getErrorHolder() > 0) {
                    requestCreator.error(action.getErrorHolder())
                }
                requestCreator.networkPolicy(NetworkPolicy.OFFLINE)
                    .into(action.getView(), object : Callback {
                        override fun onSuccess() {}
                        override fun onError(exception: Exception) {
                            //Try again online if cache failed
                            val requestCreator1: RequestCreator = picasso!!.load(action.getUrl())
                            if (action.getPlaceHolder() > 0) {
                                requestCreator1.placeholder(action.getPlaceHolder())
                            }
                            if (action.getErrorHolder() > 0) {
                                requestCreator1.error(action.getErrorHolder())
                            }
                            requestCreator1.networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(action.getView(), object : Callback {
                                    override fun onSuccess() {}
                                    override fun onError(exception: Exception) {}
                                })
                        }
                    })
            }
        })
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is INetImageProvider) 0 else 1
    }

    override fun getName(): String {
        return NAME
    }

}