package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.CreateNewPostActivity;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Post;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Profile;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment.OnListFragmentInteractionListener;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Post} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySearchedFriendsAdapter extends RecyclerView.Adapter<MySearchedFriendsAdapter.ViewHolder> {
    private final static String TAG = MySearchedFriendsAdapter.class.getSimpleName();

    private static final int CREATE_POST_CARD = 0;
    private static final int POST_CARD = 1;
    private final List<Profile> profiles;
    private final OnListFragmentInteractionListener mListener;

    public MySearchedFriendsAdapter(List<Profile> profiles, OnListFragmentInteractionListener listener) {
        this.profiles = profiles;
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView screen_name;
        TextView timestamp;
        ImageView userPhoto;
        Button btnAccept;
        Button btnIgnore;


        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_friends);
            screen_name = (TextView)itemView.findViewById(R.id.friend_req_text);
            timestamp = (TextView)itemView.findViewById(R.id.timestamp);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
            btnAccept = (Button) itemView.findViewById(R.id.btn_accept);
            btnIgnore = (Button) itemView.findViewById(R.id.btn_ignore);
        }
    }

    @Override
    public MySearchedFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friends_card, parent, false);
        return new MySearchedFriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MySearchedFriendsAdapter.ViewHolder holder, int position) {
        holder.btnAccept.setVisibility(View.GONE);
        holder.btnIgnore.setVisibility(View.GONE);
        holder.timestamp.setText(profiles.get(position).getTimestamp());
        holder.screen_name.setText(profiles.get(position).getScreen_name());
        byte[] decodedString = Base64.decode(profiles.get(position).getProfile_pic(), Base64.DEFAULT);
        holder.userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

}
