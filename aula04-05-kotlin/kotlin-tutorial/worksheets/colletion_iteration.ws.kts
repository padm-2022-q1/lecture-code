val r = (0..100)
val l1 = List<Int>(10) { _ -> r.random() }
val s1 = setOf<Int>(r.random(), r.random(), r.random())
val m1 = mapOf(r.random() to r.random(), r.random() to r.random())

for (el in l1) {
    println(el)
}

for ((k, v) in m1)
    println("$k - $v")

for (el in s1)
    println(el)

l1.forEach { it -> print("$it ") }
s1.forEach { it -> print("$it ") }
m1.forEach { k, v -> print("$k - $v ")}