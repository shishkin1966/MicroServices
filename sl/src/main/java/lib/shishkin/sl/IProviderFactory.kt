package lib.shishkin.sl

/**
 * Интерфейс Фабрики поставщиков услуг
 */
interface IProviderFactory {
    fun create(name: String): IProvider?
}
