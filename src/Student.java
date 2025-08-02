// src/Student.java

import java.time.LocalDate;

public class Student {
    private String name;
    private int classNumber;
    private String stream;
    private String father;
    private String mother;
    private LocalDate dob;
    private LocalDate doj;
    private String phone;
    private String email;

    public Student(String name, int classNumber, String stream, String father, String mother,
                   LocalDate dob, LocalDate doj, String phone, String email) {
        this.name = name;
        this.classNumber = classNumber;
        this.stream = stream;
        this.father = father;
        this.mother = mother;
        this.dob = dob;
        this.doj = doj;
        this.phone = phone;
        this.email = email;
    }

    public String getName() { return name; }
    public int getClassNumber() { return classNumber; }
    public String getStream() { return stream; }
    public String getFather() { return father; }
    public String getMother() { return mother; }
    public LocalDate getDob() { return dob; }
    public LocalDate getDoj() { return doj; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
