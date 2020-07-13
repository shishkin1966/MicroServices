package lib.shishkin.sl.lifecycle

interface ILifecycle {
    /**
     * Получить состояние объекта
     *
     * @return the state
     */
    fun getState(): Int

    /**
     * Установить состояние объекта
     *
     * @param state the state
     */
    fun setState(state: Int)
}
