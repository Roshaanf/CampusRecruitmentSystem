package roshaan.campusrecruitmentsystem;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSelectedJobInfo extends Fragment {

    TextView title;
    TextView salary;
    TextView description;
    Button delete;
    DatabaseReference ref;
    String jobNo;
    Button apply;
    Button applicants;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //agar Company feed sy call hua he to mtlb k MYJobs show krni hain us k liye key alag h
        ///company is using differnet key
        if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.CompanyFeed")) {
            jobNo = getArguments().getString(CompanyFeed.myJobUidKey);
        }

        //agr company k ilawa kahin sy call hua he to usky liye key alag
        else {   //admin and students are using same keys
            jobNo = getArguments().getString(StudentsFeed.jobNo);
        }
        return inflater.inflate(R.layout.fragment_show_selected_job_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = (TextView) getView().findViewById(R.id.frJobTitle);
        salary = (TextView) getView().findViewById(R.id.frJobSalary);
        description = (TextView) getView().findViewById(R.id.frJobDescription);
        delete = (Button) getView().findViewById(R.id.frJobDelete);
        apply = (Button) getView().findViewById(R.id.frJobApply);
        applicants = (Button) getView().findViewById(R.id.frJobApplicants);

        System.out.println("job" + jobNo);


        //checking if the activity is admin feed or caompany only thn show the delete button
        if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.AdminsFeed") ||
                getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.CompanyFeed")) {
            delete.setVisibility(View.VISIBLE);
        }
        ///means agr students feed he to  apply button show krengy wrna nh
        if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.StudentsFeed")) {

            apply.setVisibility(View.VISIBLE);
        }
        //agr company feed hgi srf tb show applicants wala show krna he
        if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.CompanyFeed")) {

            applicants.setVisibility(View.VISIBLE);
        }


        //means job has been deleted no need to go further
        if (jobNo == null) {

            //
            Toast.makeText(getActivity(), "Job has been deleted", Toast.LENGTH_SHORT).show();
        }

        //job has not been deleted
        else {

            //setting reference
            ref = FirebaseDatabase.getInstance().getReference("Jobs").child(jobNo);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    JobsStructure struct = dataSnapshot.getValue(JobsStructure.class);

                    if (struct != null) {

                        if (struct.Title != null) {
                            title.setText(struct.getTitle());
                        } else {
                            title.setText("");
                        }
                        if (struct.Description != null) {
                            description.setText(struct.getDescription());
                        } else {
                            description.setText("");
                        }
                        if (struct.Salary != null) {
                            salary.setText(struct.getSalary());
                        } else {
                            salary.setText("");
                        }
                    } else {
                        salary.setText("");
                        description.setText("");
                        title.setText("");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        //only visible in admin and company
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (jobNo == null) {
                    Toast.makeText(getActivity(), "Job has been deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // System.out.println("Job deleted before");

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Jobs").child(jobNo);
                    ref1.removeValue();
                    Toast.makeText(getActivity(), "Job has been deleted ", Toast.LENGTH_LONG).show();


                    if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.CompanyFeed")) {
                        CompanyFeed.mng.popBackStack();
                    }

                    //agr company k ilawa kahin sy call hua he to usky liye key alag
                    else {
                        AdminsFeed.mng.popBackStack();
                    }


                }
            }
        });


        //only visible in students

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (jobNo == null) {
                    Toast.makeText(getActivity(), "Job has been deleted", Toast.LENGTH_SHORT).show();
                } else {

                    //2nd line m jo hga wo sb add hjaega isliye jobNo tk pehli line m hi phnch jao refernce lety huay
                    //wrna job no b dubara add hjaega
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Jobs").child(jobNo);
                    ref1 = ref1.child("Applicants").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref1.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    Toast.makeText(getActivity(), "You have successfully applied to this job", Toast.LENGTH_LONG).show();
                }


            }
        });

        applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (jobNo != null) {
                    applicantsJobCommunicator.communicateJobNoForApplicants(jobNo);


                }

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (getActivity().getClass().getName().equals("roshaan.campusrecruitmentsystem.CompanyFeed"))
            applicantsJobCommunicator = (JobNoCommuniatorForApplicants) context;
    }

    JobNoCommuniatorForApplicants applicantsJobCommunicator;

    interface JobNoCommuniatorForApplicants {

        void communicateJobNoForApplicants(String jobNoApplicants);
    }
}