package pro.devapp.numberwatcher

import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NumberWatcherTest {
    @Test
    fun watcherTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val editText = EditText(appContext)
        val formatterNumberWatcherBuilder = NumberWatcher.Builder()
        formatterNumberWatcherBuilder.numbersAfterDecimalPoint = 2
        formatterNumberWatcherBuilder.maxValue = 100f
        formatterNumberWatcherBuilder.additional = "%"
        var resultString = ""
        val watcher = formatterNumberWatcherBuilder.build(editText) { resultString = it }
        editText.addTextChangedListener(watcher)

        editText.setText("")
        editText.setText("123")
        Assert.assertEquals("enter 123 ", "12", resultString)

        editText.setText("")
        Assert.assertEquals("enter \"\" ", "", resultString)

        editText.setText("")
        editText.setText("99")
        Assert.assertEquals("enter 99 ", "99", resultString)

        editText.setText("")
        editText.setText("99,99")
        Assert.assertEquals("enter 99,99 ", "99.99", resultString)

        editText.setText("")
        editText.setText("99.99")
        Assert.assertEquals("enter 99.99 ", "99.99", resultString)

        editText.setText("")
        editText.setText("99.999")
        Assert.assertEquals("enter 99.999 ", "99.99", resultString)

        editText.setText("")
        editText.setText("99.")
        Assert.assertEquals("enter 99. ", "99", resultString)

        editText.setText("")
        editText.setText("9.9.")
        Assert.assertEquals("enter 9.9. ", "9.9", resultString)

        editText.setText("")
        editText.append(",")
        editText.append(",")
        editText.append("9")
        editText.append(".")
        editText.append("9")
        editText.append(".")
        Assert.assertEquals("enter ,,9.9. ", "9.9", resultString)

        editText.setText("")
        editText.setText(",,9.9.")
        Assert.assertEquals("enter ,,9.9. ", "9.9", resultString)

        editText.setText("")
        editText.append("9")
        editText.append(".")
        editText.append(",")
        editText.append("9")
        editText.append(".")
        Assert.assertEquals("enter 9.,9. ", "9.9", resultString)

        editText.setText("9.,9.")
        Assert.assertEquals("enter 9.,9. ", "9.9", resultString)

        editText.setText("")
        editText.setText(null)
        Assert.assertEquals("enter null ", "", resultString)

        editText.setText("llll")
        Assert.assertEquals("enter llll ", "", resultString)

    }

    @Test
    fun watcherTestForNegativeValues() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val editText = EditText(appContext)
        val formatterNumberWatcherBuilder = NumberWatcher.Builder()
        formatterNumberWatcherBuilder.numbersAfterDecimalPoint = 2
        formatterNumberWatcherBuilder.maxValue = 100f
        formatterNumberWatcherBuilder.minValue = -200f
        formatterNumberWatcherBuilder.additional = "%"
        formatterNumberWatcherBuilder.allowNegativeValues = true
        var resultString = ""
        val watcher = formatterNumberWatcherBuilder.build(editText) { resultString = it }
        editText.addTextChangedListener(watcher)

        editText.setText("")
        editText.setText("-123")
        Assert.assertEquals("enter -123 ", "-123", resultString)

        editText.setText("")
        editText.setText("-223")
        Assert.assertEquals("enter -223 ", "-22", resultString)

        editText.setText("")
        editText.setText("-9.,9.")
        Assert.assertEquals("enter -9.,9. ", "-9.9", resultString)

        editText.setText("")
        editText.setText("--9.,9.")
        Assert.assertEquals("enter -9.,9. ", "-9.9", resultString)

        editText.setText("")
        editText.setText("--9.-,9.-")
        Assert.assertEquals("enter -9.,9. ", "-9.9", resultString)

        editText.setText("")
        editText.setText(null)
        Assert.assertEquals("enter null ", "", resultString)

        editText.setText("")
        editText.setText("llll")
        Assert.assertEquals("enter llll ", "", resultString)
    }

    @Test
    fun watcherTestMask() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val editText = EditText(appContext)
        val formatterNumberWatcherBuilder = NumberWatcher.Builder()
        formatterNumberWatcherBuilder.numbersAfterDecimalPoint = 2
        formatterNumberWatcherBuilder.maxValue = 100f
        formatterNumberWatcherBuilder.minValue = -200f
        formatterNumberWatcherBuilder.additional = "%"
        formatterNumberWatcherBuilder.allowNegativeValues = true
        formatterNumberWatcherBuilder.inputMask = "#-#-#"
        var resultString = ""
        val watcher = formatterNumberWatcherBuilder.build(editText) { resultString = it }
        editText.addTextChangedListener(watcher)

        editText.setText("")
        editText.setText("-123")
        Assert.assertEquals("enter -123 ", "-123", resultString)
        Assert.assertEquals("enter -123 ", "-1-2-3%", editText.text.toString())

        editText.setText("")
        editText.setText("-223")
        Assert.assertEquals("enter -223 ", "-22", resultString)
        Assert.assertEquals("enter -223 ", "-2-2%", editText.text.toString())

        editText.setText("")
        editText.setText("-9.,9.")
        Assert.assertEquals("enter -9.,9. ", "-9.9", resultString)
        Assert.assertEquals("enter -9.,9.", "-9-.-9%", editText.text.toString())

        editText.setText("")
        editText.setText("--9.,9.")
        Assert.assertEquals("enter -9.,9. ", "-9.9", resultString)
        Assert.assertEquals("enter -9.,9. ", "-9-.-9%", editText.text.toString())

        editText.setText("")
        editText.setText("--9.-,9.-")
        Assert.assertEquals("enter -9.,9. ", "-9.9", resultString)
        Assert.assertEquals("enter -9.,9. ", "-9-.-9%", editText.text.toString())

        editText.setText("")
        editText.setText(null)
        Assert.assertEquals("enter null ", "", resultString)
        Assert.assertEquals("enter null ", "", editText.text.toString())

        editText.setText("")
        editText.setText("llll")
        Assert.assertEquals("enter llll ", "", resultString)
        Assert.assertEquals("enter llll ", "", editText.text.toString())

        editText.setText("")
        editText.setText("-199")
        Assert.assertEquals("enter -199 ", "-199", resultString)
        Assert.assertEquals("enter -199 ", "-1-9-9%", editText.text.toString())


        formatterNumberWatcherBuilder.maxValue = 10000f
        formatterNumberWatcherBuilder.minValue = -20000f
        editText.removeTextChangedListener(watcher)
        val watcher2 = formatterNumberWatcherBuilder.build(editText) { resultString = it }
        editText.addTextChangedListener(watcher2)

        editText.setText("")
        editText.setText("-19999")
        Assert.assertEquals("enter -19999 ", "-19999", resultString)
        Assert.assertEquals("enter -19999 ", "-1-9-999%", editText.text.toString())
    }
}