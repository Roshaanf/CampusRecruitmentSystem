package roshaan.campusrecruitmentsystem;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSelectedStudentInfo extends Fragment {

    TextView skills;
    TextView education;
    TextView cgpa;
    TextView fullName;
    TextView about;
    String Email;
    Button delete;
    DatabaseReference ref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //FetchStudents will provide Email
        Email = getArguments().getString(CompanyFeed.uidKey);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_selected_student_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        about = (TextView) getView().findViewById(R.id.frStudentAbout);
        fullName = (TextView) getView().findViewById(R.id.frStudentFullName);
        education = (TextView) getView().findViewById(R.id.frStudentEducation);
        skills = (TextView) getView().findViewById(R.id.frStudentSkills);
        cgpa = (TextView) getView().findViewById(R.id.frStudentCGPA);
        delete = (Button) getView().findViewById(R.id.frStudentDelete);

        //checking if the activity is admin feed only thn show the delete button

        if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.AdminsFeed")) {
            delete.setVisibility(View.VISIBLE);
        }


        //setting reference
        ref = FirebaseDatabase.getInstance().getReference("Students").child(Email);

        //getting data
        ref.addValueEventListener(new ValueEventListener() {

            //snapshot contains pair values like FullName="ALi" CGpa=6 , we are only saving them in the form of class
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StudentStructureTwo struct = dataSnapshot.getValue(StudentStructureTwo.class);
                ;

                //to avaoid null pointer excception
                if (struct != null) {


                    if (struct.About != null) {
                        about.setText(struct.getAbout().toString());
                    } else {
                        about.setText("");
                    }
                    if (struct.CGPA != null) {
                        cgpa.setText(struct.getCGPA());
                    } else {
                        cgpa.setText("");
                    }
                    if (struct.Education != null) {
                        education.setText(struct.getEducation());
                    } else {
                        education.setText("");
                    }
                    if (struct.FullName != null) {
                        fullName.setText(struct.getFullName());
                    } else {
                        fullName.setText("");
                    }
                    if (struct.Skills != null) {
                        skills.setText(struct.getSkills());
                    } else {
                        skills.setText("");
                    }

                } else {
                    about.setText("");
                    skills.setText("");
                    fullName.setText("");
                    education.setText("");
                    cgpa.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Email == null) {
                    Toast.makeText(getActivity(), "Job has been deleted", Toast.LENGTH_SHORT).show();
                } else {

                    System.out.println("idd " + Email);
                    ref = FirebaseDatabase.getInstance().getReference("Students").child(Email);
                    ref.removeValue();
                    Toast.makeText(getActivity(), "Student has been deleted ", Toast.LENGTH_LONG).show();
                    AdminsFeed.mng.popBackStack();

                }
            }
        });


    }
}

class StudentStructureTwo {
    String About;
    String CGPA;
    String Education;
    String FullName;
    String Skills;
    String Email;
    String Uid;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    StudentStructureTwo() {

    }

    public String getCGPA() {
        return CGPA;
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

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }
}