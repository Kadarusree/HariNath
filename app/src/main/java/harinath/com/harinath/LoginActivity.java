package harinath.com.harinath;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {

    TextView mBigText, mSmallText;


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

    }

    public void login(View view) {


    }


    public void signup(View view) {
        startActivity(new Intent(getApplicationContext(), Signup.class));

    }


}
