package pro.devapp.edittextutils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class FormatterNumberWatcher private constructor(
    private val editText: EditText,
    private val listener: (text: String) -> Unit,
    private val numbersAfterDecimalPoint: Int?,
    private val maxValue: Float?,
    private val additional: String?
) : TextWatcher {

    private val re = Regex("[^0-9.,]")
    private val comaRe = Regex("[,]")
    private var isRunning = false
    private var isDeleting = false
    private val additionalLength = additional?.length ?: 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        if (isRunning) {
            return
        }
        isRunning = true

        val value =
            if (isDeleting && additional != null && !editable.toString().contains(additional)) {
                val origValue = comaRe.replace(re.replace(editable, ""), ".")
                if (additional != "" && origValue.isNotEmpty()) {
                    origValue.substring(0, origValue.length - 1)
                } else {
                    origValue
                }
            } else {
                comaRe.replace(re.replace(editable, ""), ".")
            }

        val parts = value.split(".")
        when {
            parts.size > 2 -> {
                var index = 0
                var partLength = parts[0].length
                var firstPartLength = partLength
                while (partLength == 0 && index + 1 < parts.size) {
                    index++
                    partLength = parts[index].length
                    firstPartLength += partLength
                }
                val fullString = parts.joinToString("")
                editable.replace(
                    0,
                    editable.length,
                    fullString.substring(0, firstPartLength) + "." + fullString.substring(
                        firstPartLength
                    )
                )
            }
            value == "." -> {
                editable.clear()
            }
            editable.toString() != value -> {
                editable.replace(0, editable.length, value)
            }
        }

        var clearValue = editable.toString()
        if (clearValue == ".") {
            clearValue = ""
        } else if (clearValue.length > 1 && clearValue[clearValue.length - 1] == '.') {
            clearValue = clearValue.substring(0, clearValue.length - 1)
        }

        maxValue?.let {
            if ((clearValue.toFloatOrNull() ?: 0f) > maxValue) {
                val pointPosition = editable.toString().indexOf(".")
                if (pointPosition > 0) {
                    editable.delete(pointPosition - 1, pointPosition)
                } else {
                    editable.delete(editable.length - 1, editable.length)
                }
                clearValue = editable.toString()
            }
        }

        numbersAfterDecimalPoint?.let {
            val clearParts = clearValue.split(".")
            if (editable.isNotEmpty() && numbersAfterDecimalPoint == 0 && editable.toString()[editable.length - 1] == '.') {
                editable.delete(editable.length - 1, editable.length)
                clearValue = editable.toString()
            } else if (clearParts.size == 2 && clearParts[1].length > numbersAfterDecimalPoint) {
                clearValue =
                    clearParts[0] + "." + clearParts[1].substring(0, numbersAfterDecimalPoint)
                editable.replace(0, editable.length, clearValue)
                clearValue = editable.toString()
            }
        }

        if (clearValue.isNotEmpty() && clearValue[clearValue.length - 1] == '.') {
            clearValue = clearValue.substring(0, clearValue.length - 1)
        }

        if (clearValue.isNotEmpty() && additional != null) {
            editable.replace(0, editable.length, editable.toString() + additional)
        }

        listener(clearValue)

        if (editable.length >= additionalLength && editable.length - additionalLength < editText.text.length) {
            editText.setSelection(editable.length - additionalLength)
        }

        isRunning = false
    }

    class Builder {
        /**
         * Set max digits after comma
         */
        var numbersAfterDecimalPoint: Int? = null

        /**
         * Set max value for input
         */
        var maxValue: Float? = null

        /**
         * Set string that need to added in the end
         */
        var additional: String? = null

        fun build(editText: EditText, listener: (text: String) -> Unit): FormatterNumberWatcher {
            return FormatterNumberWatcher(
                editText,
                listener,
                numbersAfterDecimalPoint,
                maxValue,
                additional
            )
        }
    }
}