// immutable list
val l1 = listOf(10, 20, 30)
val l3 = List<Int>(10, {_ -> 0})

println(l1)
println(l3)

// mutable list
val l2 = mutableListOf<Int>()
val l4 = MutableList<Int>(8, { _ -> -1 })

println(l2)
println(l4)

l2.add(10)
l2.add(20)
l2.add(30)

println(l2)

l2.set(1, 50)

println(l2)

// heterogenous types lists
val l5 = mutableListOf(1, true, "hello")

println(l5)
