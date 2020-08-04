package lib.shishkin.microservices.request

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.ImageAction
import lib.shishkin.microservices.provider.NetImageProvider
import lib.shishkin.sl.request.AbsRequest
import lib.shishkin.sl.request.Rank

class ImageRequest(private val action: ImageAction) : AbsRequest() {
    init {
        setRank(Rank.MIN_RANK)
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun run() {
        if (isValid()) {
            val provider =
                ApplicationSingleton.instance.get<NetImageProvider>(NetImageProvider.NAME)
            provider?.downloadImage(action);
        }
    }

    override fun isValid(): Boolean {
        return (super.isValid() && action.getView() != null)
    }

    override fun getName(): String {
        return ImageRequest::class.java.name
    }
}