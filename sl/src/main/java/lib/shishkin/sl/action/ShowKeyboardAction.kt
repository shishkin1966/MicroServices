package lib.shishkin.sl.action

import android.widget.EditText

class ShowKeyboardAction(private val view: EditText) : IAction {

    fun getView(): EditText {
        return view
    }

}
