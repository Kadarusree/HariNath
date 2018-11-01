package harinath.com.harinath;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import harinath.com.harinath.pojos.Fencing;
import harinath.com.harinath.pojos.UserRegPojo;


/**
 * Created by srikanthk on 7/26/2018.
 */

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MyViewHolder>

{

    private Activity mContext;
    private List<UserRegPojo> mMeetingsList;
    int selected_position = 0;

    String TAG = MeetingsAdapter.class.getName();
    GoogleApiClient googleApiClient;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mobile, email, type, meeting_for;
        final ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.meeting_title);
            mobile = (TextView) view.findViewById(R.id.meeting_description);
            email = (TextView) view.findViewById(R.id.meeting_time);
            type = (TextView) view.findViewById(R.id.meeting_venue);
            meeting_for = (TextView) view.findViewById(R.id.meeting_for);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public MeetingsAdapter(Activity mContext, List<UserRegPojo> mMeetingsList, GoogleApiClient googleApiClient) {
        this.mContext = mContext;
        this.mMeetingsList = mMeetingsList;
        this.googleApiClient = googleApiClient;
    }

    @Override
    public MeetingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new MeetingsAdapter.MyViewHolder(itemView);
    }

    int position_;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        UserRegPojo mUser = mMeetingsList.get(position);

        String fontPath = "BigTetx.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.name.setTypeface(tf);
        holder.name.setText(mUser.getFirstname() + " " + mUser.getLastname());
        holder.mobile.setText("Phone No : " + mUser.getMobileNumber());
        holder.email.setText("Email : " + mUser.getEmailID());
        holder.type.setText("User Type : " + mUser.getType());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                showPopupMenu(holder.overflow);
                position_ = position;
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMeetingsList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.options, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.cratefencing:
                    UserRegPojo mPojo = mMeetingsList.get(position_);
                    startGeofence(new LatLng(mPojo.getmLocation().getLatitude(), mPojo.getmLocation().getLongitude()));
                    saveFencingtoDB(mPojo);
                    //  dialContactPhone(mVotersList.get(selected_position).getMobileNumber());
                    return true;

                default:
            }
            return false;
        }
    }

    /////////////////////////---------Code to Create Fencing---------//////////////////////////
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 200.0f; // in meters

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;


    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(mContext, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                mContext, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission()) {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            );
        } else {
            askPermission();
        }
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    int REQ_PERMISSION = 123;// Asks for permission

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                mContext,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Start Geofence creation process
    private void startGeofence(LatLng latlng) {
        Log.i(TAG, "startGeofence()");
        Geofence geofence = createGeofence(latlng, GEOFENCE_RADIUS);

        GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
        addGeofence(geofenceRequest);

    }

    public void saveFencingtoDB(UserRegPojo mUser) {
        FirebaseDatabase mFirebaseDatabase
                = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mFirebaseDatabase.getReference("BusinessFencings");
        Fencing mPojo = new Fencing(mUser.getmLocation(),mUser.getBusinessName());
        mReference.child(mUser.getmyKey()).setValue(mPojo);
    }
}
