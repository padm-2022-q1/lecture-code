val a = 10
val b = 20
val c = 30

// if sentence
if (a > b) {
    println(a)
} else {
    println(b)
}

// nested if sentence
if (a > b && a > c ) {
    println(a)
} else if (b > a && b > c) {
    println(b)
} else if (c > a && c > b) {
    println(c)
}

// if as expression
val greater = if (a > b && a > c) {
    a
} else if (b > a && b > c) {
    b
} else {
    c
}

println(greater)

// when as sentence
when {
    a > b && a > c -> println(a)
    b > a && b > c -> println(b)
    else           -> println(c)
}

// when as expression
val greater2 = when {
    a > b && a > c -> a
    b > a && b > c -> b
    else           -> c
}

println(greater2)