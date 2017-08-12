package roshaan.campusrecruitmentsystem;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class MyJobs extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<JobsStructure> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_jobs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        data = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.fetchMyJobs);

        ref = FirebaseDatabase.getInstance().getReference().child("Jobs");

        final MyJobAdapterJobs adapter = new MyJobAdapterJobs(getActivity(), data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        ref.addValueEventListener(new ValueEventListener() {

                                      //called any time data changes in the captured part of database means the part we are referncing


                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {

                                          //it is important bbecause eveytime snapshot returns complete database so data become redundant and this method is called for complete data in one time unlike
                                          //onChildAdded
                                          data.clear();

                                          Iterable<DataSnapshot> child = dataSnapshot.getChildren();


                                          //this will contain only title salary and description for each job
                                          for (DataSnapshot ch : child) {


                                              String id = (String) ch.child("Uid").getValue();
                                              System.out.println("ye " + id);

                                              if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())) {


                                                  JobsStructure struct = ch.getValue(JobsStructure.class);


                                                  //For this fragment we only need name so now textracting names only
                                                  data.add(struct);
                                                  adapter.notifyDataSetChanged();
                                              }


                                          }
                                      }


                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {

                                      }
                                  }
        );


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        myJobCommunicator = (MyJobCommunication) context;
    }


    static MyJobCommunication myJobCommunicator;

    interface MyJobCommunication {

        void sendMyJobNo(String Jobno);
    }
}


class MyJobAdapterJobs extends RecyclerView.Adapter<MyJobViewHolderJobs> {
    LayoutInflater lf;
    Context context;
    ArrayList<JobsStructure> data;


    public MyJobAdapterJobs(Context context, ArrayList<JobsStructure> data) {


        this.context = context;
        this.data = data;

        lf = LayoutInflater.from(context);
    }

    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public MyJobViewHolderJobs onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.signle_student_row, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids
        MyJobViewHolderJobs holder = new MyJobViewHolderJobs(v, data);


        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(MyJobViewHolderJobs holder, int position) {


        //also capitalizi
        holder.name.setText(data.get(position).Title.toString().substring(0, 1).toUpperCase() + data.get(position).Title.toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
}


class MyJobViewHolderJobs extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;

    public ArrayList<JobsStructure> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyJobViewHolderJobs(View itemView, final ArrayList<JobsStructure> data) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.singleStudent);
        this.data = data;


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyJobs.myJobCommunicator.sendMyJobNo(data.get(getAdapterPosition()).JobNo);
            }
        });
    }


}