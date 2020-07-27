package lib.shishkin.microservices.observe

import android.text.Editable
import android.widget.EditText
import lib.shishkin.common.getNumbers
import lib.shishkin.common.left
import lib.shishkin.common.mid
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class EditTextDecimalObservable(observer: Observer, view: EditText, delay: Long = 500) :
    EditTextObservable(observer, view, delay) {
    private var isEditing = false

    override fun afterTextChanged(s: Editable) {
        if (isEditing) return
        isEditing = true

        var str: String? = s.toString().replace(",", ".")
        str = str?.getNumbers()?.left(13)
        val dot = "." == str?.mid(str.length - 1)
        val format = DecimalFormat("###,###.##")
        val customSymbol = DecimalFormatSymbols()
        customSymbol.decimalSeparator = '.'
        customSymbol.groupingSeparator = ' '
        format.decimalFormatSymbols = customSymbol
        var ss = format.format(str?.toDouble())
        if (dot) {
            ss += "."
        }
        if (ss != view.text.toString()) {
            view.setText(ss)
            view.setSelection(ss.length)
        }
        super.afterTextChanged(s)

        isEditing = false
    }

}
