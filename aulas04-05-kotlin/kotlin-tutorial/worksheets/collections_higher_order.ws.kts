val r = (0..100)
val l1 = List<Int>(10) { _ -> r.random() }

println(l1)

val l2 = l1.filter { it -> it > 25}
println(l2)
println(l1.map {it -> it * 2})
println(l1.reduce {it, acc -> acc + it})