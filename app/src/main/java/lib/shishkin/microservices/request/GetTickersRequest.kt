package lib.shishkin.microservices.request

import io.reactivex.Single
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.Ticker
import lib.shishkin.sl.request.AbsNetResultMessageRequest

class GetTickersRequest(subscribe: String) : AbsNetResultMessageRequest(subscribe) {
    companion object {
        const val NAME = "GetTickersRequest"
    }

    override fun getData(): Single<List<Ticker>>? {
        return ApplicationSingleton.instance.getApi().tickers
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun getName(): String {
        return NAME
    }
}
