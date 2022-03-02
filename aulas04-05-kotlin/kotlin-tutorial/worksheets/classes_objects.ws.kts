import java.lang.IllegalArgumentException

class Rectangle {
    var width: Double
        private set(w) {
            if (w < 0)
                throw IllegalArgumentException("Width must be positive")
            field = w
        }
    private var height: Double
        private set(h) {
            if (h < 0) {
                throw IllegalArgumentException("Height must be positive")
            }
            field = h
        }

    constructor(width: Double, height: Double) {
        this.width = width
        this.height = height
    }

    override fun toString(): String {
        return "w: ${width} h: ${height} a: ${area()} p: ${perimeter()}"
    }

    fun area(): Double = width * height

    fun perimeter(): Double = 2 * width + 2 * height
}

val r1 = Rectangle(10.0, 20.0)

println(r1)
println(r1.width)
//r1.width = 15.0
//r1.width = -1.0
println(r1)

//val r2 = Rectangle(15.0, -1.0)

open class Student constructor(
    var firstName: String,
    var lastName: String,
    var birthYear: Int,
    var grades: List<Double>) {

    override fun toString(): String =
        "$firstName $lastName $birthYear (${age()} yo) $grades"

    fun age() = java.time.LocalDateTime.now().year - birthYear
}

val s1 = Student("Joao", "Silva",
    1996, listOf(10.0, 2.5, 3.5))
println(s1)
s1.firstName = "Paulo"
println(s1)

class GradStudent : Student {
    var program: String
    var advisor: String

    constructor(firstName: String, lastName: String,
    birthYear: Int, grades: List<Double>, program: String,
                advisor: String) : super(firstName, lastName, birthYear, grades) {
        this.advisor = advisor
        this.program = program
    }

    override fun toString(): String {
        return super.toString() + " $program - $advisor"
    }
}

val s2 = GradStudent("Maria", "Souza", 1988, listOf(10.0, 9.0),
                        "CC", "Rodrigo Pedrosa")

println(s2)

class UnderGradStudent constructor(
    firstName: String,
    lastName: String,
    birthYear: Int,
    grades: List<Double>,
    var course: String
) : Student(firstName, lastName, birthYear, grades) {

    override fun toString(): String {
        return super.toString() + " $course"
    }
}

val s3 = UnderGradStudent("Paulo", "Siqueira", 2001, listOf(8.0), "BC&T")

println(s3)

