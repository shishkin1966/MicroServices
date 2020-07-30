package lib.shishkin.microservices.provider

import android.location.Location
import lib.shishkin.sl.IProviderSubscriber

interface ILocationSubscriber : IProviderSubscriber {

    /**
     * Установить у подписчика текущее местоположение
     *
     * @param location текущий Location
     */
    fun setLocation(location: Location)

}
