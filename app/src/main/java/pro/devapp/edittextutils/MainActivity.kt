package pro.devapp.edittextutils

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import pro.devapp.numberwatcher.NumberWatcher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val integersInput = findViewById<EditText>(R.id.integersInput)
        val integersInputWatcherBuilder = NumberWatcher.Builder()
        integersInputWatcherBuilder.numbersAfterDecimalPoint = 0 // because we need integers
        integersInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
        integersInput.addTextChangedListener(integersInputWatcherBuilder.build(integersInput) {
            Log.d("integersInput", it)
        })

        val floatsInput = findViewById<EditText>(R.id.floatsInput)
        val floatsInputWatcherBuilder = NumberWatcher.Builder()
        // optional, for example we need only to numbers after decimal point
        floatsInputWatcherBuilder.numbersAfterDecimalPoint = 2
        floatsInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
        floatsInput.addTextChangedListener(floatsInputWatcherBuilder.build(floatsInput) {
            Log.d("floatsInput", it)
        })


        val withEndStringInput = findViewById<EditText>(R.id.withEndString)
        val withEndStringInputWatcherBuilder = NumberWatcher.Builder()
        // optional, for example we need only to numbers after decimal point
        withEndStringInputWatcherBuilder.numbersAfterDecimalPoint = 2
        withEndStringInputWatcherBuilder.maxValue = 100f // optional, for example max value 100
        // optional, for example add % to the end of input
        withEndStringInputWatcherBuilder.additional = "%"
        withEndStringInput.addTextChangedListener(
            withEndStringInputWatcherBuilder.build(
                withEndStringInput
            ) {
                Log.d("withEndStringInput", it)
            })

        val negativeValuesInput = findViewById<EditText>(R.id.negativeValuesInput)
        val negativeValuesInputWatcherBuilder = NumberWatcher.Builder()
        // optional, for example we need only to numbers after decimal point
        negativeValuesInputWatcherBuilder.numbersAfterDecimalPoint = 2
        negativeValuesInputWatcherBuilder.maxValue = 100f // optional, for example max value 100
        negativeValuesInputWatcherBuilder.minValue = -200f // optional, for example min value -200
        negativeValuesInputWatcherBuilder.allowNegativeValues = true
        // optional, for example add % to the end of input
        negativeValuesInputWatcherBuilder.additional = "%"
        negativeValuesInput.addTextChangedListener(
            negativeValuesInputWatcherBuilder.build(
                negativeValuesInput
            ) {
                Log.d("negativeValuesInput", it)
            })

        val withMaskInput = findViewById<EditText>(R.id.withMaskInput)
        val withMaskInputWatcherBuilder = NumberWatcher.Builder()
        // optional, for example we need only to numbers after decimal point
        withMaskInputWatcherBuilder.numbersAfterDecimalPoint = 2
        withMaskInputWatcherBuilder.maxValue = 1000000f // optional, for example max value 100
        withMaskInputWatcherBuilder.minValue = -2000000f // optional, for example min value -200
        withMaskInputWatcherBuilder.allowNegativeValues = true
        // optional, for example add % to the end of input
        withMaskInputWatcherBuilder.additional = "%"
        withMaskInputWatcherBuilder.inputMask = "##/##/##"
        withMaskInput.addTextChangedListener(
            withMaskInputWatcherBuilder.build(
                withMaskInput
            ) {
                Log.d("withMaskInput", it)
            })
    }
}