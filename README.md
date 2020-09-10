# TextWatcher for numbers types

 - Validate user input
 - Replace "," to "."
 - Get only valid string to listener
 - Adjusting numbers after decimal point (set 0 for integer)
 - Adding any string to the input end
 - set max positive value
 - set min negative value
 - enable or disable negative values fo input
 
 ## How to use:
 
 You can see MainActivity.kt for example of usage
 
 
 ### For input only integer values
 ```kotlin
    val integersInput = findViewById<EditText>(R.id.integersInput)
    val integersInputWatcherBuilder = FormatterNumberWatcher.Builder()
    integersInputWatcherBuilder.numbersAfterDecimalPoint = 0 // because we need integers
    integersInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
    integersInput.addTextChangedListener(integersInputWatcherBuilder.build(integersInput){
        Log.d("integersInput", it)
    })
```

### For input float with fixed numbers after decimal point
```kotlin
    val floatsInput = findViewById<EditText>(R.id.floatsInput)
    val floatsInputWatcherBuilder = FormatterNumberWatcher.Builder()
    floatsInputWatcherBuilder.numbersAfterDecimalPoint = 2 // optional, for example we need only to numbers after decimal point
    floatsInputWatcherBuilder.maxValue = 1000f // optional, for example max value 1000
    floatsInput.addTextChangedListener(floatsInputWatcherBuilder.build(floatsInput){
        Log.d("floatsInput", it)
    })
```

### For adding string to the end of input
```kotlin
    val withEndStringInput = findViewById<EditText>(R.id.withEndString)
    val withEndStringInputWatcherBuilder = FormatterNumberWatcher.Builder()
    withEndStringInputWatcherBuilder.numbersAfterDecimalPoint = 2 // optional, for example we need only to numbers after decimal point
    withEndStringInputWatcherBuilder.maxValue = 100f // optional, for example max value 100
    withEndStringInputWatcherBuilder.additional = "%" // optional, for example add % to the end of input
    withEndStringInput.addTextChangedListener(withEndStringInputWatcherBuilder.build(withEndStringInput){
        Log.d("withEndStringInput", it)
    })
```