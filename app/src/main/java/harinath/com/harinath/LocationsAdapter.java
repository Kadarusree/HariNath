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
import harinath.com.harinath.pojos.HistoryPojo;
import harinath.com.harinath.pojos.UserRegPojo;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.MyViewHolder>

{

    private Activity mContext;
    private List<HistoryPojo> mHistoryPojoList;
    int selected_position = 0;

    String TAG = MeetingsAdapter.class.getName();

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


    public LocationsAdapter(Activity mContext, List<HistoryPojo> mHistoryPojoList) {
        this.mContext = mContext;
        this.mHistoryPojoList = mHistoryPojoList;
    }

    @Override
    public LocationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new LocationsAdapter.MyViewHolder(itemView);
    }

    int position_;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HistoryPojo mUser = mHistoryPojoList.get(position);

        String fontPath = "BigTetx.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.name.setTypeface(tf);
        holder.name.setText(" Time " + mUser.getTime()+"");
        holder.mobile.setText("Phone No : " + mUser.getType());
        holder.email.setText("Name : " + mUser.getName());
        holder.type.setText("Battery : " + mUser.getBattry_status());

            holder.overflow.setVisibility(View.INVISIBLE);


    }


    @Override
    public int getItemCount() {
        return mHistoryPojoList.size();
    }





}