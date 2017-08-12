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

public class AdminsFeed extends AppCompatActivity implements FetchStudents.Communication,FetchCompanies.CompanyCommunication,FetchJobs.JobsCommunication{

    static FragmentManager mng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_feed);

        FetchStudents fs=new FetchStudents();
        mng=getSupportFragmentManager();
        FragmentTransaction ftr=mng.beginTransaction();
        ftr.add(R.id.forAdminFragments,fs,"A");
        ftr.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater men=getMenuInflater();
        men.inflate(R.menu.admin_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.menuAdminStudents){
//
////            //checkkingif backstack contains any entry if it contains thn popping it out
//            if(mng.getBackStackEntryCount()>0){
//                mng.popBackStack();
//            }

            //opening Studends
            FetchStudents fs=new FetchStudents();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forAdminFragments,fs,"B");
            ftr.addToBackStack("B");
            ftr.commit();
        }

        if(item.getItemId()==R.id.menuAdminCopanies){

            //checkkingif backstack contains any entry if it contains thn popping it out
//            if(mng.getBackStackEntryCount()>0){
//                mng.popBackStack();
//            }
            //opening Companies
            mng=getSupportFragmentManager();
            FetchCompanies fc=new FetchCompanies();
            FragmentTransaction ftr=mng.beginTransaction();
            ftr.replace(R.id.forAdminFragments,fc,"C");
              ftr.addToBackStack("C");
            ftr.commit();
            System.out.println("kch nh hrha");





        }

        if(item.getItemId()==R.id.menuAdminJobs){



            //Opening Jobs
            FetchJobs fj=new FetchJobs();
            FragmentTransaction ftr=mng.beginTransaction();

            ftr.replace(R.id.forAdminFragments,fj,"d");
                ftr.addToBackStack("d");
            ftr.commit();


        }

        if(item.getItemId()==R.id.menuAdminLogout){

            //checkkingif backstack contains any entry if it contains thn popping it out
            if(mng.getBackStackEntryCount()>0){
                mng.popBackStack();
            }

            //signing out
            FirebaseAuth.getInstance().signOut();
            FirebaseAuth.getInstance();
            finish();
            startActivity(new Intent(AdminsFeed.this,Login.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void send(String email) {

        Bundle b=new Bundle();
        b.putString(CompanyFeed.uidKey,email);

        ShowSelectedStudentInfo sc=new ShowSelectedStudentInfo();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forAdminFragments,sc,"F");
        ftr.addToBackStack("F");
        ftr.commit();
    }

    @Override
    public void sendJobNo(String jobno) {

        Bundle b=new Bundle();
        b.putString(StudentsFeed.jobNo,jobno);

        ShowSelectedJobInfo sc=new ShowSelectedJobInfo();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forAdminFragments,sc,"G");
        ftr.addToBackStack("G");
        ftr.commit();
    }

    @Override
    public void sendCompanyUid(String Uid) {

        Bundle b=new Bundle();
        b.putString(StudentsFeed.uidKey,Uid);

        ShowSelectedCompanyInfo sc=new ShowSelectedCompanyInfo();
        sc.setArguments(b);

        FragmentTransaction ftr=mng.beginTransaction();
        ftr.replace(R.id.forAdminFragments,sc,"H");
        ftr.addToBackStack("H");
        ftr.commit();
    }
}
