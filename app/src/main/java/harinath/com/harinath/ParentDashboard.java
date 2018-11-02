package harinath.com.harinath;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ParentDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
    }

    public void viewFencing(View view) {
    }

    public void viewOffers(View view) {
    }

    public void trackLocations(View view) {
    }

    public void logout(View view) {
        ParentDashboard.this.finish();
    }
}
