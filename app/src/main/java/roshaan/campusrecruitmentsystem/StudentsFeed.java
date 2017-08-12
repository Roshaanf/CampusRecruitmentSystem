package roshaan.campusrecruitmentsystem;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

public class StudentsFeed extends AppCompatActivity implements FetchCompanies.CompanyCommunication, FetchJobs.JobsCommunication {


    public static final String uidKey = "Roshaan.Uid";
    public static final String jobNo = "Roshaan.jobNo";

    static FragmentManager mng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_feed);


        mng = getSupportFragmentManager();
        FetchCompanies fc = new FetchCompanies();
        FragmentTransaction ftr = mng.beginTransaction();
        ftr.add(R.id.forFragments, fc, "A");
        ftr.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.students_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuStudentCopanies) {

            //opening Companies
            mng = getSupportFragmentManager();
            FetchCompanies fc = new FetchCompanies();
            FragmentTransaction ftr = mng.beginTransaction();
            ftr.replace(R.id.forFragments, fc, "B");
            ftr.addToBackStack("B");
            ftr.commit();
            System.out.println("kch nh hrha");


        }

        if (item.getItemId() == R.id.menuStudentJobs) {


            //Opening Jobs
            FetchJobs fj = new FetchJobs();
            FragmentTransaction ftr = mng.beginTransaction();

            ftr.replace(R.id.forFragments, fj, "d");
            ftr.addToBackStack("d");
            ftr.commit();


        }

        if (item.getItemId() == R.id.menuStudentLogout) {

            //checkkingif backstack contains any entry if it contains thn popping it out
            if (mng.getBackStackEntryCount() > 0) {
                mng.popBackStack();
            }

            //signing out
            FirebaseAuth.getInstance().signOut();
            FirebaseAuth.getInstance();
            finish();
            startActivity(new Intent(StudentsFeed.this, Login.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void sendCompanyUid(String Uid) {

        Bundle b = new Bundle();
        b.putString(uidKey, Uid);

        ShowSelectedCompanyInfo sc = new ShowSelectedCompanyInfo();
        sc.setArguments(b);

        FragmentTransaction ftr = mng.beginTransaction();
        ftr.replace(R.id.forFragments, sc, "c");
        ftr.addToBackStack("c");
        ftr.commit();
    }

    @Override
    public void sendJobNo(String jobno) {

        Bundle b = new Bundle();
        b.putString(jobNo, jobno);

        ShowSelectedJobInfo sc = new ShowSelectedJobInfo();
        sc.setArguments(b);

        FragmentTransaction ftr = mng.beginTransaction();
        ftr.replace(R.id.forFragments, sc, "F");
        ftr.addToBackStack("F");
        ftr.commit();


    }
}
