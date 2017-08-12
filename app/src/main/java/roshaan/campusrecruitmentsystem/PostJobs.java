package roshaan.campusrecruitmentsystem;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostJobs extends Fragment {

    EditText title;
    EditText salary;
    EditText description;
    Button submit;
    DatabaseReference ref;
    DatabaseReference ref2;

   int jobNumber=0; //of current user
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_jobs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title=(EditText) getView().findViewById(R.id.postJobTitle);
        salary=(EditText) getView().findViewById(R.id.postJobSalary);
        description=(EditText) getView().findViewById(R.id.postJobDescription);
        submit=(Button) getView().findViewById(R.id.postJobSubmitBtn);
        auth=FirebaseAuth.getInstance();


        //only to get the job number
        ref= FirebaseDatabase.getInstance().getReference().child("Jobs");



        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //Datasnapshot will only show jobnumbers in all iterations
                //the last job number will be saved in our jobNumber variable andthen we will increment it
                //to save new job at new number
                String value=dataSnapshot.getKey().toString();
                jobNumber=Integer.parseInt(value);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //adding on click listener to button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            jobNumber++;



                ///to save info
                ref2=FirebaseDatabase.getInstance().getReference().child("Jobs")
                        .child(String.valueOf(jobNumber));

                //checking if all fields are filled
                if(!TextUtils.isEmpty(title.getText())&&
                        !TextUtils.isEmpty(salary.getText())&&
                        !TextUtils.isEmpty(description.getText()))
                {   ////Adding post to firebase
                    ref2.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref2.child("Title").setValue(title.getText().toString());
                    ref2.child("Salary").setValue(salary.getText().toString());
                    ref2.child("Description").setValue(description.getText().toString());
                    ref2.child("JobNo").setValue(String.valueOf(jobNumber));


                    //resetting values
                    title.setText(""); salary.setText(""); description.setText("");
                }
                else{

                    Toast.makeText(getActivity(),"Fill all fields",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
