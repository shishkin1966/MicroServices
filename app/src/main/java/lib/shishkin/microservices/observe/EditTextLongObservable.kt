package lib.shishkin.microservices.observe

import android.text.Editable
import android.widget.EditText
import lib.shishkin.common.left
import lib.shishkin.common.token
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import lib.shishkin.common.toLongValue
import java.util.*


class EditTextLongObservable(observer: Observer, view: EditText, delay: Long = 500) :
    EditTextObservable(observer, view, delay) {
    private var isEditing = false

    override fun afterTextChanged(s: Editable) {
        if (isEditing) return
        isEditing = true

        var str = s.toString().token("\\.", 1)
        str = str.left(10)
        val format = DecimalFormat("###,###")
        val customSymbol = DecimalFormatSymbols()
        customSymbol.decimalSeparator = '.'
        customSymbol.groupingSeparator = ' '
        format.decimalFormatSymbols = customSymbol
        val ss = format.format(str.toLongValue())
        if (ss != view.text.toString()) {
            view.setText(ss)
            view.setSelection(ss.length)
        }
        super.afterTextChanged(s)
        isEditing = false
    }
}
