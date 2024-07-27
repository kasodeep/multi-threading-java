package parallel;

public class Student {

    private final String firstName;

    private final String lastName;

    private final double age;

    private final int grade;

    private final boolean isCurrent;

    /**
     * Constructor.
     *
     * @param setFirstName Student first name
     * @param setLastName  Student last name
     * @param setAge       Student age
     * @param setGrade     Student grade in course
     * @param setIsCurrent Student currently enrolled?
     */
    public Student(final String setFirstName, final String setLastName, final double setAge, final int setGrade, final boolean setIsCurrent) {
        this.firstName = setFirstName;
        this.lastName = setLastName;
        this.age = setAge;
        this.grade = setGrade;
        this.isCurrent = setIsCurrent;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getAge() {
        return age;
    }

    public int getGrade() {
        return grade;
    }

    public boolean checkIsCurrent() {
        return isCurrent;
    }
}

