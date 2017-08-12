package roshaan.campusrecruitmentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class FetchStudents extends Fragment {


    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<String> data;
    ArrayList<StudentStructure> studentCompleteData;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fetch_students, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        data = new ArrayList<>();
        studentCompleteData = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.fetchStudents);
        ref = FirebaseDatabase.getInstance().getReference().child("Students");
        progressDialog = new ProgressDialog(getActivity());

        final MyAdapter adapter = new MyAdapter(getActivity(), data, studentCompleteData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        ref.addValueEventListener(new ValueEventListener() {

                                      //called any time data changes in the captured part of database means the part we are referncing


                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {

                                          //it is important bbecause eveytime snapshot returns complete database so data become redundant and this method is called for complete data in one time unlike
                                          //onChildAdded
                                          data.clear();
                                          studentCompleteData.clear();


                                          Iterable<DataSnapshot> child = dataSnapshot.getChildren();


                                          for (DataSnapshot ch : child) {
                                              System.out.println(ch);


                                              StudentStructure struct = ch.getValue(StudentStructure.class);

                                              //now every index of this list contains different student data
                                              studentCompleteData.add(struct);

                                              //For this fragment we only need name so now textracting names only
                                              data.add(struct.getFullName());


                                              adapter.notifyDataSetChanged();


                                          }
                                      }


                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {

                                      }
                                  }
        );

    }

    static Communication communicator;

    interface Communication {
        void send(String email);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        communicator = (Communication) context;
    }
}


class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    LayoutInflater lf;
    Context context;
    ArrayList<String> data;
    ArrayList<StudentStructure> studentCompleteData;

    public MyAdapter(Context context, ArrayList<String> data, ArrayList<StudentStructure> studentCompleteData) {

        this.context = context;
        this.data = data;
        this.studentCompleteData = studentCompleteData;
        lf = LayoutInflater.from(context);
    }

    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.signle_student_row, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids
        MyViewHolder holder = new MyViewHolder(v, data, studentCompleteData);


        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        //also capitalizi
        holder.name.setText(data.get(position).toString().substring(0, 1).toUpperCase() + data.get(position).toString().substring(1));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}


class MyViewHolder extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;
    ArrayList<StudentStructure> studentCompleteData;
    public ArrayList<String> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyViewHolder(View itemView, ArrayList<String> data, final ArrayList<StudentStructure> studentCompeteData) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.singleStudent);
        this.data = data;
        this.studentCompleteData = studentCompeteData;

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //student id on specified postition
                String st = studentCompleteData.get(getAdapterPosition()).getUid();


                //to avoid nul pointer exception this if will run if selected user have not entered his more information
                if (st == null) {
                    st = new String();
                }


                //calling function to send data to another fragment
                FetchStudents.communicator.send(st);

            }
        });
    }


}

