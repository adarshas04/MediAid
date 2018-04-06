package com.example.ankit.mediaid.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.mediaid.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "yo";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;


    Button login;
    EditText b_email,b_password;
    private ProgressDialog progressDialog;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        b_email = findViewById(R.id.emailaddress);
        b_password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        TextView textViewSignin = findViewById(R.id.textViewSignin);

        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LogInActivity.this, Sign_up.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(LogInActivity.this, MainActivity.class));

        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = b_email.getText().toString();
                String pass = b_password.getText().toString();

                if(TextUtils.isEmpty(email)){
                    b_email.setError("Please enter email");
                    return;
                }


                if(TextUtils.isEmpty(pass)){
                    b_password.setError("Please enter password");
                    return;
                }

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email , pass)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                   finish();
                                   startActivity(new Intent(LogInActivity.this,MainActivity.class));
                                }else{
                                    Toast.makeText(LogInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

       // Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

   @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(LogInActivity.this, MainActivity.class));

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId()+ acct.getEmail());

        if(FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(acct.getEmail()).getRef() == null) {
            Log.i("yo", "1");
            finish();
            startActivity(new Intent(LogInActivity.this, Sign_up.class));
            progressDialog.dismiss();
        }else {
            //getting the auth credential
            final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            //Now using firebase we are signing in the user here

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.i("yo", "2");
                                Toast.makeText(LogInActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                progressDialog.dismiss();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}