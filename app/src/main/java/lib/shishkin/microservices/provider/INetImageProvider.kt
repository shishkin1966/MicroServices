package lib.shishkin.microservices.provider

import lib.shishkin.microservices.action.ImageAction
import lib.shishkin.sl.IProvider

interface INetImageProvider : IProvider {
    /**
     * Скачать image по его url
     *
     * @param action - событие для скачивания image
     */
    fun downloadImage(action: ImageAction)

}