package roshaan.campusrecruitmentsystem;

/**
 * Created by Roshaann 2.7 gpa on 12/07/2017.
 */

public class StudentStructure {
    String About;
    String CGPA;
    String Education;
    String FullName;
    String Skills;
    String Email;
    String Uid;

    StudentStructure(){}

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getCGPA() {
        return CGPA;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {

        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setCGPA(String CGPA) {
        this.CGPA = CGPA;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSkills() {
        return Skills;
    }

    public void setSkills(String skills) {
        Skills = skills;
    }
}
