// immutable set
val s1 = setOf(5, 2, 1, 2, 3, 4, 2, 1)
val s3 = setOf<Double>()

println(s1)
println(s3)

// mutable set
val s2 = mutableSetOf(5, 2, 1, 2, 3, 4, 2, 1)
val s4: MutableSet<Double?> = mutableSetOf()

println(s2)

s2.add(10)
s2.add(4)

println(s2)

s4.add(10.0)
s4.add(20.0)
s4.add(10.0)
s4.add(null)
s4.add(null)

println(s4)