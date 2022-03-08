val m1 = mapOf("key1" to 1, "key2" to 2, "key3" to 3)

println(m1)

val m2 = mapOf("nome" to "diogo", "idade" to 39, "cidade" to "SA")

println(m2)

val m3: MutableMap<Int, Boolean> = mutableMapOf()

println(m3)

m3.put(2, true)
m3.put(3, true)
m3.put(5, false)

println(m3)

m3.put(3, false)

println(m3)