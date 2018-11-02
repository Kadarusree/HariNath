package harinath.com.harinath;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BusinessDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dashboard);
    }

    public void viewFencing(View view) {
    }

    public void viewOffers(View view) {
    }

    public void logout(View view) {
        BusinessDashboard.this.finish();
    }

}
