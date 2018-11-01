package harinath.com.harinath;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import harinath.com.harinath.pojos.UserRegPojo;


public class LoginActivity extends AppCompatActivity {

    TextView mBigText, mSmallText;
    EditText username, password;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mBigText = (TextView) findViewById(R.id.big_text);
        mSmallText = (TextView) findViewById(R.id.small_text);
        Typeface tf = Typeface.createFromAsset
                (getAssets(), "BigTetx.ttf");
        Typeface tf2 = Typeface.createFromAsset
                (getAssets(), "SmallText.ttf");
        mBigText.setTypeface(tf);
        mSmallText.setTypeface(tf2);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabse.getReference("Users");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.setCancelable(false);
    }

    public void login(View view) {

        mProgressDialog.show();
        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        mProgressDialog.setMessage("Signing In");
                        if (task.isSuccessful()) {
                            mProgressDialog.show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            mDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mProgressDialog.dismiss();
                                    if (dataSnapshot != null) {
                                        UserRegPojo mUser = dataSnapshot.getValue(UserRegPojo.class);
                                        Toast.makeText(getApplicationContext(), "Welcome " + mUser.getFirstname() + "", Toast.LENGTH_LONG).show();

                                        Constants.username = mUser.getFirstname() + "/" + mUser.getLastname();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();
                                }
                            });
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //   Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });

    }


    public void signup(View view) {
        startActivity(new Intent(getApplicationContext(), Signup.class));

    }


    public void AdminLogin(View view) {
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checklocation();
    }

    public void checklocation(){
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setMessage("Please Enable Location");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                }
            });
            dialog.show();
        }
    }
}
