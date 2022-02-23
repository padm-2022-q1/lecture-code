import kotlin.math.roundToInt

// type inference
var aInteger = 10
val aLong = 10L
var aDouble = 10.0
val aFloat = 10.0f
val aBoolean = true
val aString = "Hello"

println(aInteger)

// all kotlin types are reference types
println(aInteger.times(aInteger))
println(aLong.plus(aLong))
println(aDouble.div(aInteger))
println(aFloat.roundToInt())
println(aBoolean.and(false))
println(aString.uppercase())