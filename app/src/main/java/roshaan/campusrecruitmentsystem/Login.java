package roshaan.campusrecruitmentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loginButton;
    TextView goForSignup;
    FirebaseAuth auth;
    ProgressDialog progressDialogue;
    int checkForDeletedAccount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=(EditText) findViewById(R.id.loginEmail);
        password =(EditText) findViewById(R.id.loginPassword);
        loginButton=(Button) findViewById(R.id.loginButton);
        goForSignup=(TextView) findViewById(R.id.loginDontHaveAccount);

        //getting the auth instance
        auth=FirebaseAuth.getInstance();

        progressDialogue=new ProgressDialog(this);
    }

    // Called when login button will be pressed
    public void loginClicked(View v){

        String email=this.email.getText().toString();
        String password=this.password.getText().toString();

        //chcking if email and password are not empty
        if(!TextUtils.isEmpty(email)&&
                !TextUtils.isEmpty(password)  ){

            progressDialogue.setMessage("Logging in please wait");
            progressDialogue.show();

            //loging user in auth
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                //to check if the tassk successfully completed or not otherwise no need to go fo .addOnCompleteListener
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if(task.isSuccessful()){
                        System.out.println("Kamyaab");
                        //Login succcessfull  now deciding which feed  to open

                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("Admin").child(uid);

                        ref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){

                                    progressDialogue.dismiss();

                                    //removing this activity from back stack
                                    finish();
                                    //opening studentsFeed
                                    Intent newIntent = new Intent(Login.this,AdminsFeed.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);

                                }
                                else{
                                    checkForDeletedAccount++;

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

                                    progressDialogue.dismiss();
                                    //removing this activity from back stack
                                    finish();

                                    //opening company feed
                                    startActivity(new Intent(Login.this,CompanyFeed.class));
                                }
                                else{
                                    checkForDeletedAccount++;

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
                                System.out.println("admin");
                                if(dataSnapshot.exists()){

                                    progressDialogue.dismiss();
                                    //removing this activity from back stack
                                    finish();

                                    //opening admin feed
                                    startActivity(new Intent(Login.this,StudentsFeed.class));
                                }
                                else{
                                    checkForDeletedAccount++;
                                    if(checkForDeletedAccount>=3){
                                        progressDialogue.dismiss();
                                        Toast.makeText(Login.this,"Login failed! incorred username or password",Toast.LENGTH_LONG).show();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    else{  progressDialogue.dismiss();
                        Toast.makeText(Login.this,"Login failed! incorred username or password",Toast.LENGTH_LONG).show();


                    }
                }
            });

        }else{

            Toast t= Toast.makeText(this,"Enter email and password",Toast.LENGTH_LONG);
            t.show();
        }

    }

    //onClick method of textview
    public void openSignup(View v){

        //removing current actiity from back stack and closing
        finish();

        //opening signup activity
        startActivity(new Intent(this,Signup.class));
    }
}
