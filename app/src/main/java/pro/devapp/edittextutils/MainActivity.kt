package pro.devapp.edittextutils

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val integersInput = findViewById<EditText>(R.id.integersInput)
        val integersInputWatcherBuilder = FormatterNumberWatcher.Builder()
        integersInputWatcherBuilder.numbersAfterDecimalPoint = 0 // because we need integers
        integersInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
        integersInput.addTextChangedListener(integersInputWatcherBuilder.build(integersInput) {
            Log.d("integersInput", it)
        })

        val floatsInput = findViewById<EditText>(R.id.floatsInput)
        val floatsInputWatcherBuilder = FormatterNumberWatcher.Builder()
        floatsInputWatcherBuilder.numbersAfterDecimalPoint =
            2 // optional, for example we need only to numbers after decimal point
        floatsInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
        floatsInput.addTextChangedListener(floatsInputWatcherBuilder.build(floatsInput) {
            Log.d("floatsInput", it)
        })


        val withEndStringInput = findViewById<EditText>(R.id.withEndString)
        val withEndStringInputWatcherBuilder = FormatterNumberWatcher.Builder()
        withEndStringInputWatcherBuilder.numbersAfterDecimalPoint =
            2 // optional, for example we need only to numbers after decimal point
        withEndStringInputWatcherBuilder.maxValue = 100f // optional, for example max value 100
        withEndStringInputWatcherBuilder.additional =
            "%" // optional, for example add % to the end of input
        withEndStringInput.addTextChangedListener(
            withEndStringInputWatcherBuilder.build(
                withEndStringInput
            ) {
                Log.d("withEndStringInput", it)
            })
    }
}