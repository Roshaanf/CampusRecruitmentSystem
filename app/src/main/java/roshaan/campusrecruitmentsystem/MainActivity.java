package roshaan.campusrecruitmentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    int forAccountCheck=0;
    FirebaseAuth.AuthStateListener authListener;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);




        auth=FirebaseAuth.getInstance();






                if(auth.getCurrentUser()==null){
                    System.out.println("no one is logged in");
                    //remving this activity from backstack
                    finish();
                    //Starting login activity
                    Intent i=new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                }
                else{
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    System.out.println("User "+user+" is logged in");

                    //Deciding which feed to show
                    progressDialog.setMessage("Gathering information please wait!");
                    progressDialog.show();

                    final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    System.out.println("UID"+uid);
                   DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("Admin").child(uid);

                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                System.out.println("hello");
                                progressDialog.dismiss();
                                //removing this activity from back stack
                                finish();
                                //opening studentsFeed
                                startActivity(new Intent(MainActivity.this,AdminsFeed.class));
                            } else{
                                forAccountCheck++;
                                //if for account checck here has value >=3 it means that id is not present in either of tables
                                if(forAccountCheck>=3){

                                    progressDialog.dismiss();

                                    Toast.makeText(MainActivity.this,"No user is logged in ",Toast.LENGTH_LONG).show();
                                    finish();

                                    //opening login activity
                                    startActivity(new Intent(MainActivity.this,Login.class));
                                }
                                System.out.println("#"+forAccountCheck);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("Company").child(uid);
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                            progressDialog.dismiss();
                            //removing this activity from back stack
                                finish();

                                //opening company feed
                                startActivity(new Intent(MainActivity.this,CompanyFeed.class));
                            } else{

                                forAccountCheck++;
                                //if for account checck here has value >=3 it means that id is not present in either of tables
                                if(forAccountCheck>=3){
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this,"No user is logged in ",Toast.LENGTH_LONG).show();
                                    finish();

                                    //opening login activity
                                    startActivity(new Intent(MainActivity.this,Login.class));
                                }
                                System.out.println("!"+forAccountCheck);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference ref3=FirebaseDatabase.getInstance().getReference("Students").child(uid);
                    ref3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                progressDialog.dismiss();

                                //if user has not filled all his info then profile info page will be showed
                                DatabaseReference ref5= FirebaseDatabase.getInstance().getReference().child("Students")
                                        .child(uid).child("Skills");

                                ref5.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        //means student has filled all his data so no need to call Profileinfo activity
                                        if(dataSnapshot.exists()){

                                            //removing thi activity from backstack
                                            finish();
                                            //opening student feed
                                            startActivity(new Intent(MainActivity.this,StudentsFeed.class));
                                        }
                                        //means info has not filled so open profileinfo actitvity
                                        else{

                                            //removing this activity from back stack
                                            finish();
                                            //opening profile info

                                            startActivity(new Intent(MainActivity.this,StudentProfilenfo.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                            } else{
                                forAccountCheck++;
                                //if for account checck here has value >=3 it means that id is not present in either of tables
                                if(forAccountCheck>=3){

                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this,"No user is logged in ",Toast.LENGTH_LONG).show();
                                    finish();

                                    //opening login activity
                                    startActivity(new Intent(MainActivity.this,Login.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }






    }



