package interview.practice.functional.streams.challenges;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Challenge 6: Student With Highest Mark For A Major.
 */
public class StreamChallenge6_StudentWithHighestMark {

    public static void main(String[] args) {
        List<Student> students = SampleData.students();
        Optional<Integer> highest = Answer.findHighestGradeForMajor(students, "Computer Science");
        System.out.println("Highest grade for CS: " + highest.orElse(0));
    }
}

final class Answer {

    private Answer() {}

    static Optional<Integer> findHighestGradeForMajor(List<Student> students, String major) {
        return students.stream()
                .filter(student -> major.equalsIgnoreCase(student.major()))
                .flatMap(student -> student.grades().values().stream())
                .max(Integer::compareTo);
    }
}

record Student(String firstName, String lastName, String major, Map<String, Integer> grades) {}

final class SampleData {
    private SampleData() {}

    static List<Student> students() {
        return List.of(
                new Student("Ananya", "Sharma", "Computer Science", Map.of("DSA", 91, "Algo", 88)),
                new Student("Rohit", "Verma", "Electronics", Map.of("Circuits", 83, "Signals", 78)),
                new Student("Meera", "Iyer", "Computer Science", Map.of("Networks", 95, "DBMS", 89))
        );
    }
}
