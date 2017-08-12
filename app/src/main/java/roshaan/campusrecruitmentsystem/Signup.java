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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText email;
    EditText password;
    RadioGroup radioGroup;
    Button signupButton;
    FirebaseAuth auth;
    RadioButton radioButton;
    DatabaseReference ref;
    EditText fullName;
    TextView goForLogin;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//    FirebaseUser u=auth.getCurrentUser();

        //getting refernce
        auth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.signupEmail);
        password = (EditText) findViewById(R.id.signupPassword);
        signupButton = (Button) findViewById(R.id.signupButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        fullName = (EditText) findViewById(R.id.signupFullName);
        progressDialog = new ProgressDialog(this);
        goForLogin = (TextView) findViewById(R.id.alreadyHavAccnt);


        //opening login activity
        goForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(new Intent(Signup.this, Login.class));
            }
        });

    }


    public void signupClicked(View v) {

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        final String fullName = this.fullName.getText().toString();

        //checking if email and password are not null
        if (!TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(fullName)) {

            System.out.println(email + password);

            //creating user in auth
            progressDialog.setMessage("Signing up please wait");
            progressDialog.show();

            //creating a new user means signup
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                //checking if the signup has completed successfully
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //getting which radiobutton is checked inside radiogroup
                        int id = radioGroup.getCheckedRadioButtonId();
                        ///finding id of the checked button
                        radioButton = (RadioButton) findViewById(id);
                        System.out.println(radioButton.getText().toString());

                        //if student is selceted it means the account belongs to student
                        //and open StudentProfileInfo activity
                        //otherwise go for company inside profile page

                        progressDialog.dismiss();
                        if (radioButton.getText().toString().equals("Student")) {

                            //enetring student data in database
                            ref = FirebaseDatabase.getInstance().getReference("Students");
                            //ref.child(auth.getCurrentUser().getEmail().replace(".","%2E")).child("FullName").setValue(fullName);
                            ref.child(auth.getCurrentUser().getUid()).child("FullName").setValue(fullName);
                            ref.child(auth.getCurrentUser().getUid()).child("Uid").setValue(auth.getCurrentUser().getUid());
                            //closing this activity and removing from back stack
                            finish();
                            //opening StudentProfileInfoActivity
                            //Userinfo will be addedd to database in StudentProfileActivity

                            //opening profile activity and clearing complete bacckstacck
                            Intent newIntent = new Intent(Signup.this, StudentProfilenfo.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newIntent);

                        }
                        //means Company is selected so enter company data in database
                        else {
                            //enetring company data in database
                            ref = FirebaseDatabase.getInstance().getReference("Company").child(auth.getCurrentUser().getUid());
                            // ref.child(auth.getCurrentUser().getEmail().replace(".","%2E")).child("FullName").setValue(fullName);
                            ref.child("FullName").setValue(fullName);
                            ref.child("Email").setValue(auth.getCurrentUser().getEmail());
                            ref.child("Uid").setValue(auth.getCurrentUser().getUid());


                            //opening companyfeed
                            finish();
                            startActivity(new Intent(Signup.this, CompanyFeed.class));
                        }


                        System.out.println("Kamyaabi");
                        //  System.out.println(task.getResult().getUser());
                    } else {
                        // If sign in fails, display a message to the user.


                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {

                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Signup failed password must be 6 characters long", Toast.LENGTH_LONG).show();

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Signup failed! Bad structure of email", Toast.LENGTH_LONG).show();
                        } catch (FirebaseAuthUserCollisionException e) {

                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Signup failed id already in use choose another one", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                }
            });

        } else {


            Toast t = Toast.makeText(this, "Fill all fields", Toast.LENGTH_LONG);
            t.show();
        }

    }
}
