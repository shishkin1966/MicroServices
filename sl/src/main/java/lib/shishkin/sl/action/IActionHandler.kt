package lib.shishkin.sl.action

interface IActionHandler {
    /**
     * Обработать событие
     *
     * @param action  событие
     */
    fun onAction(action: IAction): Boolean

}
