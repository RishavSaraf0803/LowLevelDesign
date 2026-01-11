
package interview.practice.functional.streams.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Challenge 5: Parallel Stream Performance
 * 
 * Task: Compare performance between sequential and parallel streams.
 * 
 * Requirements:
 * - Create a large dataset (1 million+ integers)
 * - Perform complex operations (filter, map, reduce) on both sequential and parallel streams
 * - Measure execution time
 * - Analyze when parallel streams are beneficial
 * - Discuss thread safety considerations
 */
public class StreamChallenge5_ParallelStreamPerformance {
    
    public static void main(String[] args) {
        Stream<Employee> sequential = createLargeDataSet();
        long start = System.currentTimeMillis();
        long totalPay = sequential
                .filter(employee -> employee.age() > 30)
                .mapToLong(Employee::salary)
                .sum();
        System.out.printf("Sequential total pay: %d (time=%dms)%n", totalPay, System.currentTimeMillis() - start);

        Stream<Employee> parallel = createLargeDataSet().parallel();
        start = System.currentTimeMillis();
        totalPay = parallel
                .filter(employee -> employee.age() > 30)
                .mapToLong(Employee::salary)
                .sum();
        System.out.printf("Parallel total pay: %d (time=%dms)%n", totalPay, System.currentTimeMillis() - start);
    }

    private static Stream<Employee> createLargeDataSet() {
        int datasetSize = 1_000_000;
        List<String> departments = Arrays.asList("HR", "Engineering", "Sales", "Finance", "Marketing");
        return IntStream.range(0, datasetSize)
            .mapToObj(i -> new Employee(
                "Employee" + i,
                20 + (i % 45), // age between 20 and 64
                30_000 + (i % 120_000), // salary between 30k and 150k
                new Department(departments.get(i % departments.size()))
            ));
    }

    record Employee(String name, int age, long salary, Department department) {}

    record Department(String name) {}
}
