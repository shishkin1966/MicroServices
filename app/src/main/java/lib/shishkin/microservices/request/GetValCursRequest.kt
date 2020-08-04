package lib.shishkin.microservices.request

import io.reactivex.Single
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.ValCurs
import lib.shishkin.sl.request.AbsNetResultMessageRequest

class GetValCursRequest(subscribe: String, private val date: String) :
    AbsNetResultMessageRequest(subscribe) {
    companion object {
        const val NAME = "GetValCursRequest"
    }

    override fun getData(): Single<ValCurs>? {
        return ApplicationSingleton.instance.getCbApi().getData(date)
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun getName(): String {
        return NAME
    }
}
