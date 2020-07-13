package lib.shishkin.common;

import java.io.File
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


fun CharSequence?.isNullOrEmpty(): Boolean {
    return this == null || this.toString().trim { it <= ' ' }.length < 1
}

fun String?.isNullOrEmpty(): Boolean {
    return this == null || trim { it <= ' ' }.length < 1
}

fun String?.pos(which: String?, start: Int): Int {
    if (this == null || which == null) {
        return -1;
    }
    return if (start >= this.length) -1 else this.indexOf(which, start);
}

fun String?.token(
    delims: String?,
    tokenNumber: Int
): String {
    var delimiters = delims
    if (this == null) {
        return ""
    }
    if (this.length == 0) {
        return ""
    }
    if (delimiters == null) {
        return ""
    }
    if (delimiters.length == 0) {
        return ""
    }

    if (delimiters == "(") {
        delimiters = "\\("
    }
    if (delimiters == ")") {
        delimiters = "\\)"
    }

    val arr = this.split(delimiters.toRegex()).toTypedArray()
    var count = 0
    for (i in arr.indices) {
        if (!arr[i].isNullOrEmpty()) {
            count++
            if (count == tokenNumber) {
                return arr[i]
            }
        }
    }
    return ""
}

fun String?.left(length: Int): String {
    if (this == null) {
        return ""
    }
    return if (length >= this.length) {
        this
    } else this.substring(0, length)
}

fun String?.getDigits(): String? {
    return if (this.isNullOrEmpty()) {
        this
    } else this!!.replace("\\D+".toRegex(), "")
}

fun String?.getNumbers(): String? {
    return if (this.isNullOrEmpty()) {
        this
    } else this!!.replace("[^\\.0123456789\\+]".toRegex(), "")
}

fun String?.toLong(): Long {
    var l: Long = 0
    if (!this.isNullOrEmpty()) {
        try {
            val str = this.getDigits()
            l = java.lang.Long.parseLong(str!!)
        } catch (e: Exception) {
        }

    }
    return l
}

fun String?.toInt(): Int {
    var i = 0
    if (!this.isNullOrEmpty()) {
        try {
            val str = this.getDigits()
            i = Integer.parseInt(str!!)
        } catch (e: Exception) {
        }

    }
    return i
}

fun String?.toDouble(): Double {
    var s: String? = this
    var d = 0.0
    if (!s.isNullOrEmpty()) {
        s = s!!.replace(",", ".")
        s = s.getNumbers()
        try {
            d = java.lang.Double.parseDouble(s!!)
        } catch (e: Exception) {
        }
    }
    return d
}

fun String?.toFloat(): Float {
    var s: String? = this
    var f = 0f
    if (!s.isNullOrEmpty()) {
        s = s!!.replace(",", ".")
        s = s.getNumbers()
        try {
            f = java.lang.Float.parseFloat(s!!)
        } catch (e: Exception) {
        }
    }
    return f
}

fun String?.replace(
    replaceString: String?, replaceWith: String?
): String {
    // Verify parameters
    if (this == null || replaceString == null
        || replaceWith == null
    ) {
        return ""
    }

    // Return intial string if what to replae and replace with are equals
    if (replaceString == replaceWith) {
        return this
    }

    // Keep the value
    var temp: String = this

    // Set initial value
    var startPos = 0

    // Find the first occurrence of as_replace_it
    startPos = temp.indexOf(replaceString, startPos)

    // Only enter the loop if you find as_replace_it
    while (startPos >= 0) {
        // Replace as_replace_it with as_replace_with
        temp = (temp.substring(0, startPos)
                + replaceWith
                + temp.substring(
            startPos + replaceString.length,
            temp.length
        ))

        // Find the next occurrence of as_replace_it
        startPos = temp.indexOf(
            replaceString,
            startPos + replaceWith.length
        )
    }

    return temp
}

fun String?.mid(first: Int, lenString: Int): String {
    var len = lenString
    if (this == null) {
        return ""
    }
    if (this.length == 0) {
        return ""
    }
    if (first > this.length) {
        return ""
    }
    if (first + len > this.length) {
        len = this.length - first
    }
    return this.substring(first, first + len)
}

fun String?.mid(first: Int): String {
    if (this == null) {
        return ""
    }
    if (this.length == 0) {
        return ""
    }
    return if (first > this.length) {
        this
    } else this.substring(first, this.length)
}

fun Double?.double2String(): String {
    if (this == null) return ""
    val format = DecimalFormat("###,##0.00")
    val customSymbol = DecimalFormatSymbols()
    customSymbol.setDecimalSeparator('.')
    customSymbol.setGroupingSeparator(' ')
    format.setDecimalFormatSymbols(customSymbol)
    return format.format(this)
}

fun BigDecimal?.decimal2String(): String {
    if (this == null) return ""
    val format = DecimalFormat("###,##0.00")
    val customSymbol = DecimalFormatSymbols()
    customSymbol.decimalSeparator = '.'
    customSymbol.groupingSeparator = ' '
    format.decimalFormatSymbols = customSymbol
    return format.format(this)
}

fun String?.trimZero(): String? {
    if (this == null) return null
    if (!this.contains(".")) return this
    if (this == ".") return this

    var length = this.length
    val cnt = length - 1
    for (i in cnt downTo 0) {
        val chr = this.mid(i, 1)
        if ("0" == chr) {
            length--
        } else if ("." == chr) {
            length--
            break
        } else {
            break
        }
    }
    return if (length == 0) {
        ""
    } else this.mid(0, length)
}

fun String?.trimAllZero(): String? {
    if (this == null) return null
    if (!this.contains(".")) return this
    if (this == ".") return this

    val arr = this.split(" ".toRegex()).toTypedArray()
    val sb = StringBuilder()
    for (i in arr.indices) {
        if (!arr[i].isNullOrEmpty()) {
            sb.append(arr[i].trimZero())
            sb.append(" ")
        }
    }
    return sb.toString().allTrim()
}

fun String?.allTrim(): String {
    return if (this.isNullOrEmpty()) {
        ""
    } else this!!.trim()
}

fun Long.formatDateShortRu(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun String.getDir(): String {
    try {
        val file = File(this)
        return file.parent
    } catch (e: Exception) {
        return ""
    }
}
