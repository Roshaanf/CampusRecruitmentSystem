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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FetchCompanies extends Fragment {


    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<CompanyStructure> data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fetch_companies, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.fetchCompanies);
        ref = FirebaseDatabase.getInstance().getReference().child("Company");

        final MyAdapterCompanies adapter = new MyAdapterCompanies(getActivity(), data);

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


                                          for (DataSnapshot ch : child) {
                                              System.out.println(ch);


                                              CompanyStructure struct = ch.getValue(CompanyStructure.class);
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
        communicator = (CompanyCommunication) context;
    }

    static CompanyCommunication communicator;

    interface CompanyCommunication {

        void sendCompanyUid(String Uid);
    }
}


class MyAdapterCompanies extends RecyclerView.Adapter<MyViewHolderCompanies> {
    LayoutInflater lf;
    Context context;
    ArrayList<CompanyStructure> data;
    ArrayList<StudentStructure> studentCompleteData;

    public MyAdapterCompanies(Context context, ArrayList<CompanyStructure> data) {

        this.context = context;
        this.data = data;

        lf = LayoutInflater.from(context);
    }

    //inflation will take place here
    //this method will be called if new viewHolder rewuired otherwise recycling will do the job
    @Override
    public MyViewHolderCompanies onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = lf.inflate(R.layout.signle_student_row, parent, false);
        //sending inflatd view to ViewHolder so that it can find resource ids
        MyViewHolderCompanies holder = new MyViewHolderCompanies(v, data);


        return holder;
    }

    //Responsible to fill UI
    //The holder parameter here is from onCreateHolder's return statement so it contains ids
    @Override
    public void onBindViewHolder(MyViewHolderCompanies holder, int position) {

        //also capitalizi
        holder.name.setText(data.get(position).FullName.toString().substring(0, 1).toUpperCase() + data.get(position).FullName.toString().substring(1));

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
}


class MyViewHolderCompanies extends RecyclerView.ViewHolder {

    DatabaseReference ref;
    TextView name;

    public ArrayList<CompanyStructure> data;

    //This will be called inside onCreateViewHolder
    //parameter interView contains the inflated single row view
    //here we will find resource ids
    public MyViewHolderCompanies(View itemView, final ArrayList<CompanyStructure> data) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.singleStudent);
        this.data = data;


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //this method will be called in activity

                FetchCompanies.communicator.sendCompanyUid(data.get(getAdapterPosition()).Uid);
            }
        });
    }


}


class CompanyStructure {

    String FullName;
    String Email;
    String Uid;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    CompanyStructure() {

    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}