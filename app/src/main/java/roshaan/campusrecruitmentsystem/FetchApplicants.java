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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//https://stackoverflow.com/questions/41848856/how-to-read-query-data-from-one-userid-firebase-android

/**
 * A simple {@link Fragment} subclass.
 */
public class FetchApplicants extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<String> data;
    ArrayList<StudentStructure> studentCompleteData;
    String jobNoApplicants;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        jobNoApplicants=getArguments().getString(CompanyFeed.jobNoApplicantsKey);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_applicants, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        studentCompleteData = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.fetchApplicants);
        ref = FirebaseDatabase.getInstance().getReference().child("Jobs").child(jobNoApplicants).child("Applicants");


        final ApplicantsAdapter adapter = new ApplicantsAdapter(getActivity(), data, studentCompleteData);

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

                                          //contains the children of the captured part in our case it contains emails and thn data inside each email table
                                          //basically we are one step below in our ddatabasse now from where we referncced
                                          Iterable<DataSnapshot> child = dataSnapshot.getChildren();


                                          for (DataSnapshot ch : child) {
                                              System.out.println(ch);
                                              //because we have made proper getter and setter and same naming convention as in databasse it will
                                              //automatically assign all corresposind values

                                              DatabaseReference reff=FirebaseDatabase.getInstance().getReference().child("Students").child((String)ch.getValue());


                                               reff.addValueEventListener(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(DataSnapshot dataSnapshot) {

                                                     StudentStructure struct=dataSnapshot.getValue(StudentStructure.class);

                                                       if(struct!=null){
                                                       studentCompleteData.add(struct);


                                                       data.add(struct.getFullName());



                                                       adapter.notifyDataSetChanged();

                                                   }
                                                   }

                                                   @Override
                                                   public void onCancelled(DatabaseError databaseError) {

                                                   }
                                               });


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
        aplicantsIDProvider= (JobApplicantsIDProvider) context;
    }

    static JobApplicantsIDProvider aplicantsIDProvider;
    interface JobApplicantsIDProvider{


        void sendSelectedJobApplicantsId(String uidApplicant);
    }
}



class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsViewHolder>
{
    LayoutInflater lf;
    Context context;
    ArrayList<String> data;
    ArrayList<StudentStructure> studentCompleteData;

    public ApplicantsAdapter(Context context,ArrayList<String> data, ArrayList<StudentStructure> studentCompleteData){


        this.context=context;
        this.data=data;
        this.studentCompleteData=studentCompleteData;
        lf=LayoutInflater.from(context);
    }
    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public ApplicantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v=lf.inflate(R.layout.signle_student_row,parent,false);
        //sending inflatd view to ViewHolder so that it can find resource ids
        ApplicantsViewHolder holder=new ApplicantsViewHolder(v,data,studentCompleteData);

        //returning holder , holder now contains the ids of the layout
        //values will be assigned to these ids in onBindViewHolder
        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(ApplicantsViewHolder holder, int position) {


        //also capitalizi
        holder.name.setText(data.get(position).toString().substring(0,1).toUpperCase()+ data.get(position).toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
}


class ApplicantsViewHolder extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;
    ArrayList<StudentStructure> studentCompleteData;
    public  ArrayList<String> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public ApplicantsViewHolder(View itemView, ArrayList<String> data, final ArrayList<StudentStructure> studentCompeteData) {
        super(itemView);
        name=(TextView) itemView.findViewById(R.id.singleStudent);

        this.data=data;
        this.studentCompleteData=studentCompeteData;

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //student id on specified postition
                String st=studentCompleteData.get(getAdapterPosition()).getUid();


                //to avoid nul pointer exception this if will run if selected user have not entered his more information
                if(st==null){
                    st=new String();
                }


                //calling function to send data to another fragment
                FetchApplicants.aplicantsIDProvider.sendSelectedJobApplicantsId(st);

            }
        });
    }


}