package parallel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A simple wrapper class for various analytics methods.
 */
public final class StudentAnalytics {

    /**
     * Sequentially computes the average age of all actively enrolled students
     * using loops.
     *
     * @param studentArray Student data for the class.
     * @return Average age of enrolled students
     */
    public double averageAgeOfEnrolledStudentsImperative(final Student[] studentArray) {
        List<Student> activeStudents = new ArrayList<>();

        for (Student s : studentArray) {
            if (s.checkIsCurrent()) {
                activeStudents.add(s);
            }
        }

        double ageSum = 0.0;
        for (Student s : activeStudents) {
            ageSum += s.getAge();
        }
        return ageSum / (double) activeStudents.size();
    }

    /**
     * Compute the average age of all actively enrolled students using
     * parallel streams. This should mirror the functionality of
     * averageAgeOfEnrolledStudentsImperative. This method should not use any
     * loops.
     *
     * @param studentArray Student data for the class.
     * @return Average age of enrolled students
     */
    public double averageAgeOfEnrolledStudentsParallelStream(final Student[] studentArray) {
        OptionalDouble average = Arrays
                .stream(studentArray)
                .parallel()
                .filter(Student::checkIsCurrent)
                .mapToDouble(Student::getAge)
                .average();

        if (average.isEmpty()) return 0.0;
        return average.getAsDouble();
    }

    /**
     * Sequentially computes the most common first name out of all students that
     * are no longer active in the class using loops.
     *
     * @param studentArray Student data for the class.
     * @return Most common first name of inactive students
     */
    public String mostCommonFirstNameOfInactiveStudentsImperative(final Student[] studentArray) {
        List<Student> inactiveStudents = new ArrayList<>();

        for (Student s : studentArray) {
            if (!s.checkIsCurrent()) {
                inactiveStudents.add(s);
            }
        }

        Map<String, Integer> nameCounts = new HashMap<>();
        for (Student s : inactiveStudents) {
            nameCounts.put(s.getFirstName(), nameCounts.getOrDefault(s.getFirstName(), 0) + 1);
        }

        String mostCommon = null;
        int mostCommonCount = -1;

        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            if (mostCommon == null || entry.getValue() > mostCommonCount) {
                mostCommon = entry.getKey();
                mostCommonCount = entry.getValue();
            }
        }
        return mostCommon;
    }

    /**
     * Compute the most common first name out of all students that are no
     * longer active in the class using parallel streams. This should mirror the
     * functionality of mostCommonFirstNameOfInactiveStudentsImperative. This
     * method should not use any loops.
     *
     * @param studentArray Student data for the class.
     * @return Most common first name of inactive students
     */
    public String mostCommonFirstNameOfInactiveStudentsParallelStream(final Student[] studentArray) {
        return Arrays.stream(studentArray)
                .parallel()
                .filter(n -> !n.checkIsCurrent())
                .collect(Collectors.groupingBy(Student::getFirstName, Collectors.counting()))
                .entrySet()
                .parallelStream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Sequentially computes the number of students who have failed the course
     * who are also older than 20 years old. A failing grade is anything below a
     * 65. A student has only failed the course if they have a failing grade, and
     * they are not currently active.
     *
     * @param studentArray Student data for the class.
     * @return Number of failed grades from students older than 20 years old.
     */
    public int countNumberOfFailedStudentsOlderThan20Imperative(final Student[] studentArray) {
        int count = 0;
        for (Student s : studentArray) {
            if (!s.checkIsCurrent() && s.getAge() > 20 && s.getGrade() < 65) {
                count++;
            }
        }
        return count;
    }

    /**
     * Compute the number of students who have failed the course who are
     * also older than 20 years old. A failing grade is anything below a 65. A
     * student has only failed the course if they have a failing grade, and they
     * are not currently active. This should mirror the functionality of
     * countNumberOfFailedStudentsOlderThan20Imperative. This method should not
     * use any loops.
     *
     * @param studentArray Student data for the class.
     * @return Number of failed grades from students older than 20 years old.
     */
    public int countNumberOfFailedStudentsOlderThan20ParallelStream(final Student[] studentArray) {

        return (int) Arrays.stream(studentArray)
                .parallel()
                .filter(n -> !n.checkIsCurrent() && n.getAge() > 20 && n.getGrade() < 65)
                .count();
    }
}
