// ranges
(1..4).forEach { it -> println(it) }
println()
(1..10 step 2).forEach {it -> println(it)}
println()
(4 downTo 1).forEach {it -> println(it)}

println(1 in 1..4)
println(3 in 1..4)
println(5 in 1..4)

// for
for (i in 1..4) println(i)
println()
for (i in 6 downTo 0 step 2) println(i)