fun greater(a: Int, b: Int, c: Int): Int {
    return when {
        a > b && a > c -> a
        b > a && b > c -> b
        else           -> c
    }
}

println(greater(10, 20, 30))
println(greater(30, 20, 10))
println(greater(20, 10, 30))

// simplified function with assigment operator
fun greater2(a: Int, b: Int, c: Int): Int =
    when {
        a > b && a > c -> a
        b > a && b > c -> b
        else           -> c
    }

println(greater2(10, 20, 30))
println(greater2(30, 20, 10))
println(greater2(20, 10, 30))

// function overload
fun greater(a: Int, b: Int): Int = if (a > b) a else b

println(greater(10, 20))
println(greater(20, 10))

// lambda
val greaterOf2Ref = {a: Int, b: Int -> if (a > b) a else b}

println(greaterOf2Ref(10, 20))
println(greaterOf2Ref(20, 10))

val greaterOf3Ref: (Int, Int, Int) -> Int = {a, b, c ->
    when {
        a > b && a > c -> a
        b > a && b > c -> b
        else           -> c
    }
}

println(greaterOf3Ref(10, 20, 30))
println(greaterOf3Ref(30, 20, 10))
println(greaterOf3Ref(20, 10, 30))

fun lesser(a: Int, b: Int, c: Int): Int =
    when {
        a < b && a < c -> a
        b < a && b < c -> b
        else -> c
    }

println(lesser(10, 20, 30))
println(lesser(30, 20, 10))
println(lesser(20, 10, 30))

// higher order function
fun getOne(a: Int, b: Int, c: Int, f: (Int, Int, Int) -> Int): Int =
    f(a, b, c)

fun getOne(a: Int, b: Int, f: (Int, Int) -> Int): Int =
    f(a, b)

println(getOne(10, 20, 30, ::greater))
println(getOne(10, 20, 30, greaterOf3Ref))
println(getOne(10, 20, 30, ::lesser))

println(getOne(10, 20, ::greater))
println(getOne(10, 20, {a, b -> if (a < b) a else b}))
