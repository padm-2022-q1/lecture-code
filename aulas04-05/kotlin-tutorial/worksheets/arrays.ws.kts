import java.util.Arrays

val v1 = arrayOf(1, 2, 3, 4)
val v2 = arrayOf(1.0, 2.0, 3.0, 4.0)
val v3 = arrayOf(1, true, 1.0, "hello")
val v4 = intArrayOf(1, 2, 3, 4)
val v5 = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
val v6 = booleanArrayOf(true, false, false, true)

println(Arrays.toString(v1))
v1[1] = 5
println(Arrays.toString(v1))

val v7 = Array<Int>(5, { i -> -1 })

println(Arrays.toString(v7))