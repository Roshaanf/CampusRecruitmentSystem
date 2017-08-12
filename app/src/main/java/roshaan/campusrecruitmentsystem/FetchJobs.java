package roshaan.campusrecruitmentsystem;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FetchJobs extends Fragment {


    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<JobsStructure> data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fetch_jobs, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.fetchJobs);

        ref = FirebaseDatabase.getInstance().getReference().child("Jobs");

        final MyAdapterJobs adapter = new MyAdapterJobs(getActivity(), data);

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

                                              JobsStructure struct = ch.getValue(JobsStructure.class);


                                              //For this fragment we only need name so now textracting names only
                                              data.add(struct);


                                              adapter.notifyDataSetChanged();


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

        communicator = (JobsCommunication) context;
    }

    static JobsCommunication communicator;

    interface JobsCommunication {

        void sendJobNo(String String);
    }
}


class MyAdapterJobs extends RecyclerView.Adapter<MyViewHolderJobs> {
    LayoutInflater lf;
    Context context;
    ArrayList<JobsStructure> data;


    public MyAdapterJobs(Context context, ArrayList<JobsStructure> data) {


        this.context = context;
        this.data = data;

        lf = LayoutInflater.from(context);
    }

    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public MyViewHolderJobs onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.signle_student_row, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids
        MyViewHolderJobs holder = new MyViewHolderJobs(v, data);

        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(MyViewHolderJobs holder, int position) {


        //also capitalizi
        holder.name.setText(data.get(position).Title.toString().substring(0, 1).toUpperCase() + data.get(position).Title.toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
}


class MyViewHolderJobs extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;

    public ArrayList<JobsStructure> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyViewHolderJobs(View itemView, final ArrayList<JobsStructure> data) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.singleStudent);

        this.data = data;


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FetchJobs.communicator.sendJobNo(data.get(getAdapterPosition()).JobNo.toString());
            }
        });
    }


}


class JobsStructure {

    String Description;
    String Title;
    String Salary;
    String JobNo;
    String Uid;

    public JobsStructure() {

    }

    public String getJobNo() {
        return JobNo;
    }

    public void setJobNo(String jobNo) {
        JobNo = jobNo;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }
}

