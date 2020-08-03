package lib.shishkin.microservices.provider

import lib.shishkin.sl.IProvider
import lib.shishkin.microservices.action.ImageAction

interface INetImageProvider : IProvider {
    /**
     * Скачать image по его url
     *
     * @param action - событие для скачивания image
     */
    fun downloadImage(action: ImageAction)

}