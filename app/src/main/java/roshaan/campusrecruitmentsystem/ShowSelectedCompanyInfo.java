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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSelectedCompanyInfo extends Fragment {

    String uid;
    DatabaseReference ref;
    TextView name;
    Button delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        uid = getArguments().getString(StudentsFeed.uidKey);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_selected_company_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name = (TextView) getView().findViewById(R.id.frCompanyFullname);
        delete = (Button) getView().findViewById(R.id.frCompanyDelete);

        //if activity is admins feed only then set visibility
        if (getActivity().getClass().getName().toString().equals("roshaan.campusrecruitmentsystem.AdminsFeed")) {
            delete.setVisibility(View.VISIBLE);
        }

        ref = FirebaseDatabase.getInstance().getReference("Company").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CompanyStructure struc = dataSnapshot.getValue(CompanyStructure.class);

                if (struc != null) {

                    if (struc.Email != null) {

                        name.setText(struc.getEmail());
                    }
                } else {
                    name.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid == null) {
                    Toast.makeText(getActivity(), "Company has been deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // System.out.println("Job deleted before");

                    //removing Company
                    ref = FirebaseDatabase.getInstance().getReference("Company").child(uid);
                    ref.removeValue();

                    //removing job posted by this company


                    DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference().child("Jobs");

                    ref5.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                            for (DataSnapshot ch : child) {

                                //getting uid and job of child
                                String value = (String) ch.child("Uid").getValue();
                                String job = (String) ch.child("JobNo").getValue();

                                if (value.equals(uid)) {

                                    FirebaseDatabase.getInstance().getReference().child("Jobs").child(job).removeValue();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    Toast.makeText(getActivity(), "Company has been deleted ", Toast.LENGTH_LONG).show();
                    AdminsFeed.mng.popBackStack();

                }

            }
        });

    }


    ArrayList<String> jobRemover() {
        final ArrayList<String> jobs = new ArrayList<String>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Jobs");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                for (DataSnapshot ch : child) {

                    String value = (String) ch.child("Uid").getValue();

                    if (value.equals(uid)) {
                        jobs.add(ch.child("JobNo").toString());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return jobs;
    }
}
