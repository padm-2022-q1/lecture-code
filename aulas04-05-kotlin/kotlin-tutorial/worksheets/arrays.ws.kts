import java.util.Arrays

val v1 = arrayOf(1, 2, 3, 4)
val v2 = arrayOf(1.0, 2.0, 3.0, 4.0)
val v3 = arrayOf(1, true, 1.0, "hello")
val v4 = intArrayOf(1, 2, 3, 4)
val v5 = doubleArrayOf(5.0, 8.0, 9.0)
val v6 = Array<Int>(5, { i -> i * 2 + 1})

println(Arrays.toString(v6))

// arrays are mutable
v6[1] = 29
println(Arrays.toString(v6))