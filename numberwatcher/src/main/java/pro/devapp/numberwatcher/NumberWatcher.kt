package pro.devapp.numberwatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class NumberWatcher private constructor(
    private val editText: EditText,
    private val listener: (text: String) -> Unit,
    private val numbersAfterDecimalPoint: Int?,
    private val maxValue: Float?,
    private val minValue: Float?,
    private val additional: String?,
    private val inputMask: String?,
    allowNegativeValues: Boolean = false
) : TextWatcher {

    private val regex = if (allowNegativeValues) Regex("[^0-9.,\\-]") else Regex("[^0-9.,]")
    private val comaRegex = Regex("[,]")
    private val minusRegex = Regex("[-]")
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

        removeUnnecessaryChars(editable)

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
                } else if (editable.isNotEmpty()) {
                    editable.delete(editable.length - 1, editable.length)
                }
                clearValue = editable.toString()
            }
        }

        minValue?.let {
            val floatValue = (clearValue.toFloatOrNull() ?: 0f)
            if (floatValue < minValue && floatValue < 0) {
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

        inputMask?.let { mask ->
            val editableLength = editable.length
            val inputMaskLength = mask.length
            val maskedValue = StringBuffer()
            val isNegativeValue = (editable.toString().toFloatOrNull() ?: 0f) < 0f
            var offset = 0
            for (i in 0 until inputMaskLength) {
                if (editable.length <= offset) {
                    break
                }
                if (isNegativeValue && i < 1) {
                    if (editable.isEmpty()) {
                        break
                    }
                    maskedValue.append(editable[0])
                    offset++
                    continue
                }
                if (mask[i - if (isNegativeValue) 1 else 0] != '#') {
                    maskedValue.append(mask[i - if (isNegativeValue) 1 else 0])
                } else {
                    maskedValue.append(editable[offset])
                    offset++
                }
            }
            if (editableLength > offset) {
                maskedValue.append(editable.toString().substring(offset))
            }
            editable.replace(0, editable.length, maskedValue)
        }

        if (clearValue.isNotEmpty() && additional != null) {
            editable.append(additional)
        }

        listener(clearValue)

        if (editable.length >= additionalLength && editable.length - additionalLength < editText.text.length) {
            editText.setSelection(editable.length - additionalLength)
        }

        isRunning = false
    }

    private fun getValue(editable: Editable): String {
        return if (isDeleting && additional != null && !editable.toString().contains(additional)) {
            val origValue = comaRegex.replace(regex.replace(editable, ""), ".")
            if (additional != "" && origValue.isNotEmpty()) {
                origValue.substring(0, origValue.length - 1)
            } else {
                origValue
            }
        } else {
            comaRegex.replace(regex.replace(editable, ""), ".")
        }
    }

    private fun removeUnnecessaryChars(editable: Editable) {
        var value = getValue(editable)
        if (value.contains("-")) {
            value = "-" + minusRegex.replace(value, "")
            editable.replace(0, editable.length, value)
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
    }

    class Builder {
        /**
         * Set max digits after comma
         */
        var numbersAfterDecimalPoint: Int? = null

        /**
         * Set max value for input
         * must be more than 0
         */
        var maxValue: Float? = null

        /**
         * Set min value for input
         * Only for negative values
         * must be less than 0
         */
        var minValue: Float? = null

        /**
         * Set string that need to added in the end
         */
        var additional: String? = null

        /**
         * Set string input mask
         * each number mark as #
         */
        var inputMask: String? = null

        /**
         * Allow input negative values
         */
        var allowNegativeValues: Boolean = false

        fun build(editText: EditText, listener: (text: String) -> Unit): NumberWatcher {
            maxValue?.let {
                if (it <= 0f) {
                    throw Exception("must be more than 0")
                }
            }
            minValue?.let {
                if (it >= 0) {
                    throw Exception("must be less than 0")
                } else if (!allowNegativeValues) {
                    allowNegativeValues = true
                }
            }
            return NumberWatcher(
                editText,
                listener,
                numbersAfterDecimalPoint,
                maxValue,
                minValue,
                additional,
                inputMask,
                allowNegativeValues
            )
        }
    }
}