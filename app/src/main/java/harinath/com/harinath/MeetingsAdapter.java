package harinath.com.harinath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import harinath.com.harinath.pojos.UserRegPojo;


/**
 * Created by srikanthk on 7/26/2018.
 */

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MyViewHolder>

{

    private Context mContext;
    private List<UserRegPojo> mMeetingsList;
    int selected_position = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, mobile, email, type, meeting_for;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.meeting_title);
            mobile = (TextView) view.findViewById(R.id.meeting_description);
            email = (TextView) view.findViewById(R.id.meeting_time);
            type = (TextView) view.findViewById(R.id.meeting_venue);
            meeting_for = (TextView) view.findViewById(R.id.meeting_for);
        }
    }


    public MeetingsAdapter(Context mContext, List<UserRegPojo> mMeetingsList) {
        this.mContext = mContext;
        this.mMeetingsList = mMeetingsList;
    }

    @Override
    public MeetingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new MeetingsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        UserRegPojo mUser = mMeetingsList.get(position);

        String fontPath = "BigTetx.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.name.setTypeface(tf);
        holder.name.setText(mUser.getFirstname()+" "+mUser.getLastname());
        holder.mobile.setText("Phone No : " + mUser.getMobileNumber());
        holder.email.setText("Email : " + mUser.getEmailID());
        holder.type.setText("User Type : " + mUser.getType());


    }


    @Override
    public int getItemCount() {
        return mMeetingsList.size();
    }



}
