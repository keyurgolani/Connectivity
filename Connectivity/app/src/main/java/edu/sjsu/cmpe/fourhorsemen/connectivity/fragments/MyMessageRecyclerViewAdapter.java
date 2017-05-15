package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Message;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.MessageFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Post} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private final List<Message> messages;
    private final OnListFragmentInteractionListener mListener;

    public MyMessageRecyclerViewAdapter(List<Message> messages, OnListFragmentInteractionListener listener) {
        this.messages = messages;
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView screen_name;
        TextView timestamp;
        TextView subject;
        ImageView userPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_message);
            screen_name = (TextView)itemView.findViewById(R.id.screen_name);
            timestamp = (TextView)itemView.findViewById(R.id.timestamp);
            subject = (TextView)itemView.findViewById(R.id.subject);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.screen_name.setText(messages.get(position).getFrom().getScreen_name());
        holder.timestamp.setText(messages.get(position).getTimestamp());
        holder.subject.setText(messages.get(position).getContent());
        byte[] decodedString = Base64.decode(messages.get(position).getFrom().getProfile_pic(), Base64.DEFAULT);
        holder.userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
