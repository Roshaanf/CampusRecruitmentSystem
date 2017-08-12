package roshaan.campusrecruitmentsystem;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class CompanyFeed extends AppCompatActivity implements FetchStudents.Communication,MyJobs.
        MyJobCommunication , ShowSelectedJobInfo.JobNoCommuniatorForApplicants ,FetchApplicants.JobApplicantsIDProvider
{

    public static final String uidKey="Roshaan.UiDcompany";
    public static final String  myJobUidKey="Roshaan.MYJob.Uid";
    public static final String jobNoApplicantsKey="Roshaan.JobNoApplicants";
   // public  static final String selectedApplicantsIsKey="Roshaan.SelectedApplicantsKEy";

   static FragmentManager mng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_feed);

        FetchStudents fs=new FetchStudents();
        mng=getSupportFragmentManager();
        FragmentTransaction ftr=mng.beginTransaction();
        ftr.add(R.id.forCompanyFragments,fs,"A");
        ftr.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater men=getMenuInflater();
        men.inflate(R.menu.company_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.menuCompanyStudents){

//            //checkkingif backstack contains any entry if it contains thn popping it out
//            if(mng.getBackStackEntryCount()>0){
//                mng.popBackStack();
//            }

            //opening Studends
            FetchStudents fs=new FetchStudents();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forCompanyFragments,fs,"B");
            ftr.addToBackStack("B");
            ftr.commit();
        }

        if(item.getItemId()==R.id.menuCompanyAddJobs){

//            checkkingif backstack contains any entry if it contains thn popping it out
//            if(mng.getBackStackEntryCount()>0){
//                mng.popBackStack();
//            }

            //opening job posts fragment
            PostJobs ps=new PostJobs();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forCompanyFragments,ps,"C");
            ftr.addToBackStack("C");
            ftr.commit();
        }

        if(item.getItemId()==R.id.menuCompanyMyJobs){

            //            checkkingif backstack contains any entry if it contains thn popping it out
//            if(mng.getBackStackEntryCount()>0){
//                mng.popBackStack();
//            }

            //opening job posts fragment
            MyJobs ps=new MyJobs();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forCompanyFragments,ps,"L");
            ftr.addToBackStack("L");
            ftr.commit();


        }
        if(item.getItemId()==R.id.menuCompanyLogout){

//            checkkingif backstack contains any entry if it contains thn popping it out
//            if(mng.getBackStackEntryCount()>0){
//                mng.popBackStack();
//            }


            finish();
            startActivity(new Intent(CompanyFeed.this,Login.class));
            //signing out
            FirebaseAuth.getInstance().signOut();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void send(String email) {

        Bundle b=new Bundle();
        b.putString(uidKey,email);

        ShowSelectedStudentInfo sc=new ShowSelectedStudentInfo();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forCompanyFragments,sc,"F");
        ftr.addToBackStack("F");
        ftr.commit();
    }

    @Override
    public void sendMyJobNo(String Jobno) {

        Bundle b=new Bundle();
        b.putString(myJobUidKey,Jobno);

        ShowSelectedJobInfo sc=new ShowSelectedJobInfo();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forCompanyFragments,sc,"J");
        ftr.addToBackStack("J");
        ftr.commit();

    }

    @Override
    public void communicateJobNoForApplicants(String jobNoApplicants) {

        Bundle b=new Bundle();
        b.putString(jobNoApplicantsKey,jobNoApplicants);

        FetchApplicants sc=new FetchApplicants();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forCompanyFragments,sc,"P");
        ftr.addToBackStack("P");
        ftr.commit();

    }

    @Override
    public void sendSelectedJobApplicantsId(String uidApplicant) {

        Bundle b=new Bundle();
        b.putString(uidKey,uidApplicant);

        ShowSelectedStudentInfo sc=new ShowSelectedStudentInfo();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forCompanyFragments,sc,"K");
        ftr.addToBackStack("K");
        ftr.commit();

    }
}
