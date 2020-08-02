package lib.shishkin.microservices.provider

import lib.shishkin.sl.IProvider
import lib.shishkin.sl.provider.AbsNetProvider
import lib.shishkin.sl.provider.INetProvider

class NetProvider : AbsNetProvider<NetApi>() {
    companion object {
        const val NAME = "NetProvider"
        const val URL = "https://api.coinmarketcap.com/" //Базовый адрес
    }

    override fun getApiClass(): Class<NetApi> {
        return NetApi::class.java
    }

    override fun getBaseUrl(): String {
        return URL
    }

    override fun getName(): String {
        return NAME
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is INetProvider<*>) 0 else 1
    }

}
