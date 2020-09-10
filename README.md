# TextWatcher for numbers types

 - Validate user input
 - Replace "," to "."
 - Get only valid string to listener
 - Adjusting numbers after decimal point (set 0 for integer)
 - Adding any string to the input end
 - set max positive value
 - set min negative value
 - enable or disable negative values fo input
 - add text mask (for example ##/##/##)
 
 ## Additional I created some useful extension functions
 
 you can find it in EditTextExtensions.kt
 
 - addTextListener - for easier adding text change listener (don't use with NumberWatcher)
 - setActionDoneListener - for listen "done" event
 - setNumberWatcher - for easier adding NumberWatcher
 
 ## How to use NumberWatcher:
 
 You can see more examples in MainActivity.kt or NumberWatcherTest.kt
 
 ### For input only integer values
 ```kotlin
    val integersInput = findViewById<EditText>(R.id.integersInput)
    val integersInputWatcherBuilder = NumberWatcher.Builder()
    integersInputWatcherBuilder.numbersAfterDecimalPoint = 0 // because we need integers
    integersInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
    integersInput.addTextChangedListener(integersInputWatcherBuilder.build(integersInput){
        Log.d("integersInput", it)
    })
```

### For input float with fixed numbers after decimal point
```kotlin
    val floatsInput = findViewById<EditText>(R.id.floatsInput)
    val floatsInputWatcherBuilder = NumberWatcher.Builder()
    floatsInputWatcherBuilder.numbersAfterDecimalPoint = 2 // optional, for example we need only to numbers after decimal point
    floatsInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
    floatsInput.addTextChangedListener(floatsInputWatcherBuilder.build(floatsInput){
        Log.d("floatsInput", it)
    })
```

### For adding string to the end of input
```kotlin
    val withEndStringInput = findViewById<EditText>(R.id.withEndString)
    val withEndStringInputWatcherBuilder = NumberWatcher.Builder()
    withEndStringInputWatcherBuilder.numbersAfterDecimalPoint = 2 // optional, for example we need only to numbers after decimal point
    withEndStringInputWatcherBuilder.maxValue = 100f // optional, for example max value 100
    withEndStringInputWatcherBuilder.additional = "%" // optional, for example add % to the end of input
    withEndStringInput.addTextChangedListener(withEndStringInputWatcherBuilder.build(withEndStringInput){
        Log.d("withEndStringInput", it)
    })
```

### If you want to allow to users enter values between -100 and 100
```kotlin
val negativeValuesInput = findViewById<EditText>(R.id.negativeValuesInput)
val negativeValuesInputWatcherBuilder = NumberWatcher.Builder()
// optional, for example we need only to numbers after decimal point
negativeValuesInputWatcherBuilder.numbersAfterDecimalPoint = 2
negativeValuesInputWatcherBuilder.maxValue = 100f // optional, for example max value 100
negativeValuesInputWatcherBuilder.minValue = -100f // optional, for example min value -100
negativeValuesInputWatcherBuilder.allowNegativeValues = true
// optional, for example add % to the end of input
negativeValuesInputWatcherBuilder.additional = "%"
negativeValuesInput.addTextChangedListener(negativeValuesInputWatcherBuilder.build(negativeValuesInput) {
        Log.d("negativeValuesInput", it)
    })
```

### If you want to add mask
```kotlin
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
withMaskInput.addTextChangedListener(withMaskInputWatcherBuilder.build(withMaskInput) {
        Log.d("withMaskInput", it)
    })
```