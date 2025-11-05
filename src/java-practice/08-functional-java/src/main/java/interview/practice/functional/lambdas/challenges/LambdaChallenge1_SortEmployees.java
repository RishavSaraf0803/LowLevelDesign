package interview.practice.functional.lambdas.challenges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Challenge 1: Sort Employees.
 *
 * Sort by name (ascending) and salary (descending when names tie).
 */
public class LambdaChallenge1_SortEmployees {

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        EmployeeFactory.prepareEmployees(employees);

        print("Before sort", employees);
        Collections.sort(employees);
        print("After sort", employees);
    }

    private static void print(String label, List<Employee> list) {
        System.out.println("--- " + label + " ---");
        list.forEach(System.out::println);
    }
}

record Employee(String name, long salary, Department department) implements Comparable<Employee> {

    @Override
    public int compareTo(Employee other) {
        int byName = this.name().compareTo(other.name());
        if (byName != 0) {
            return byName;
        }
        return Long.compare(other.salary(), this.salary());
    }
}

record Department(String name) {}

final class EmployeeFactory {
    private EmployeeFactory() {}

    static void prepareEmployees(List<Employee> employees) {
        employees.add(new Employee("Alice", 90000, new Department("Engineering")));
        employees.add(new Employee("Bob", 100000, new Department("HR")));
        employees.add(new Employee("Charlie", 95000, new Department("Engineering")));
        employees.add(new Employee("David", 90000, new Department("Marketing")));
        employees.add(new Employee("Eve", 120000, new Department("HR")));
        employees.add(new Employee("Bob", 110000, new Department("Engineering")));
        employees.add(new Employee("Alice", 95000, new Department("Marketing")));
    }
}
