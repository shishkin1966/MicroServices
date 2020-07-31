package lib.shishkin.microservices.provider

import lib.shishkin.sl.IProvider
import lib.shishkin.sl.provider.AbsNetProvider
import lib.shishkin.sl.provider.INetProvider
import retrofit2.Converter
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class NetCbProvider : AbsNetProvider<NetCbApi>() {

    companion object {
        const val NAME = "NetCbProvider"
        const val URL = "http://www.cbr.ru/" //Базовый адрес
    }

    override fun getApiClass(): Class<NetCbApi> {
        return NetCbApi::class.java
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

    override fun getConverterFactory(): Converter.Factory {
        return SimpleXmlConverterFactory.create()
    }

}