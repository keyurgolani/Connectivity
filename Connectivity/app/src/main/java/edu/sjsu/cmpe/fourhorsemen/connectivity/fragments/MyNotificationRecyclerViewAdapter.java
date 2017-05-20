package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Notification;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.NotificationFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Post} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNotificationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = MyNotificationRecyclerViewAdapter.class.getSimpleName();

    private static final int CREATE_NOTIFICATION_CARD = 0;
    private static final int NOTIFICATION_CARD = 1;
    private final List<Notification> notifications;
    private final OnListFragmentInteractionListener mListener;

    public MyNotificationRecyclerViewAdapter(List<Notification> notifications, OnListFragmentInteractionListener listener) {
        this.notifications = notifications;
        mListener = listener;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView userName;
        TextView content;
        TextView timestamp;

        NotificationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_notification);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            content = (TextView) itemView.findViewById(R.id.notification_content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        NotificationViewHolder nvholder = (NotificationViewHolder) holder;
        nvholder.timestamp.setText(notifications.get(position).getTimestamp());
        nvholder.content.setText(notifications.get(position).getText());

        nvholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    int profileToOpen = notifications.get(position).getFriend();
                    // TODO: Need to open a new activity for viewing others' profile on click using `profileToOpen`
                    mListener.onListFragmentInteraction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
