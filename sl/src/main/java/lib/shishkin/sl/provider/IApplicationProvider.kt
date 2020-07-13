package lib.shishkin.sl.provider

import lib.shishkin.sl.IProvider

interface IApplicationProvider : IProvider {
    /**
     * Флаг - выход из приложения
     *
     * @return true = приложение остановлено
     */
    fun isExit(): Boolean

    /**
     * Отправить приложение в фон
     *
     */
    fun toBackground()
}
