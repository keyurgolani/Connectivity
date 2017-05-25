package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Request;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.FriendsListsFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Request} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyFriendsRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendsRecyclerViewAdapter.ViewHolder> {

    private final List<Request> requests;
    private final OnListFragmentInteractionListener mListener;

    public MyFriendsRecyclerViewAdapter(List<Request> requests, OnListFragmentInteractionListener listener) {
        this.requests = requests;
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView requestText;
        TextView timestamp;
        ImageView userPhoto;
        Button btnAccept;
        Button btnIgnore;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_friends);
            requestText = (TextView)itemView.findViewById(R.id.friend_req_text);
            timestamp = (TextView)itemView.findViewById(R.id.timestamp);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
            btnAccept = (Button) itemView.findViewById(R.id.btn_accept);
            btnIgnore = (Button) itemView.findViewById(R.id.btn_ignore);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friends_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(requests.get(position).getRequest_type().equals("received")){
            holder.requestText.setText(requests.get(position).getScreen_name()+" has sent you a friend request.");
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnIgnore.setVisibility(View.VISIBLE);
        }
        else{
            holder.requestText.setText(requests.get(position).getScreen_name());
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnIgnore.setVisibility(View.GONE);
        }
        holder.timestamp.setText("");
        byte[] decodedString = Base64.decode(requests.get(position).getProfile_pic(), Base64.DEFAULT);
        holder.userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

}
