import java.io.Serializable;

public class Person implements Serializable {
    private String firstName;
    private String surname;
    private int age;

    public Person (String firstName, String surname, int age)
    {
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getSurname()
    {
        return this.surname;
    }

    public int getAge()
    {
        return this.age;
    }

    



}
