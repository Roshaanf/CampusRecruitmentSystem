package roshaan.campusrecruitmentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentProfilenfo extends AppCompatActivity {

    EditText overview;
    EditText skills;
    EditText cgpa;
    EditText education;
    Button submit;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profilenfo);

        overview=(EditText) findViewById(R.id.studentProfileOverview);
        cgpa=(EditText) findViewById(R.id.studentProfileCGPA);
        education=(EditText) findViewById(R.id.studentProfileEducation);
        skills=(EditText) findViewById(R.id.studentProfileSkills);
        submit=(Button) findViewById(R.id.studentProfileSubmitBtn);
        progressDialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
    }

    public void submit(View v){


        String overview=this.overview.getText().toString();
        String skills=this.skills.getText().toString();
        String cgpa=this.cgpa.getText().toString();
        String education=this.education.getText().toString();
        progressDialog.setMessage("Entering information! please wait");
        progressDialog.show();

        //getting current user's email
        String email=FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(!TextUtils.isEmpty(overview)
                &&!TextUtils.isEmpty(skills)
                &&!TextUtils.isEmpty(cgpa)
                &&!TextUtils.isEmpty(education)
                ){

            //taking refernce of current email in students table
            ref=FirebaseDatabase.getInstance().getReference("Students").child(email);

            //adding skillls
            ref.child("Skills").setValue(skills);
            //adding cgpa
            ref.child("CGPA").setValue(cgpa);
            //adding skillls
            ref.child("Education").setValue(education);
            //adding skillls
            ref.child("About").setValue(overview);

            //adding email
            ref.child("Email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","%2E"));

           progressDialog.dismiss();

            //now closing this activity
            finish();

            //procedding to students feed
            startActivity(new Intent(this,StudentsFeed.class));

        }
        //if all fields are not filled
        else{
            progressDialog.dismiss();
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_LONG).show();
        }


    }
}
