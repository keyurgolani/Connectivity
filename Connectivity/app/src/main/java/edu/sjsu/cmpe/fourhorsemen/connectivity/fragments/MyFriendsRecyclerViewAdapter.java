package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Request;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.FriendsListsFragment.OnListFragmentInteractionListener;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(requests.get(position).getRequest_type().equals("received")){
            holder.requestText.setText(requests.get(position).getScreen_name()+" has sent you a friend request.");
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnIgnore.setVisibility(View.VISIBLE);
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("unique_id", PreferenceHandler.getAccessKey());
                    params.put("friend", requests.get(position).getProfile());
                    RequestHandler.HTTPRequest(holder.userPhoto.getContext(), ProjectProperties.METHOD_ACCEPT_FRIEND, params, new ResponseHandler() {
                        @Override
                        public void handleSuccess(JSONObject response) throws Exception {
                            if(response.getInt("status_code") == 200) {
                                Snackbar.make(holder.userPhoto.getRootView(), "Friend Request Accepted", Snackbar.LENGTH_SHORT).show();
                                holder.btnAccept.setVisibility(View.GONE);
                                holder.btnIgnore.setVisibility(View.GONE);
                                holder.requestText.setText(requests.get(position).getScreen_name()+" is a friend now!");
                            } else {
                                Snackbar.make(holder.userPhoto.getRootView(), "Internal Error. Please try again later!", Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void handleError(Exception e) throws Exception {
                            e.printStackTrace();
                        }
                    });
                }
            });
            holder.btnIgnore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("unique_id", PreferenceHandler.getAccessKey());
                    params.put("friend", requests.get(position).getProfile());
                    RequestHandler.HTTPRequest(holder.userPhoto.getContext(), ProjectProperties.METHOD_DECLINE_REQUEST, params, new ResponseHandler() {
                        @Override
                        public void handleSuccess(JSONObject response) throws Exception {
                            if(response.getInt("status_code") == 200) {
                                Snackbar.make(holder.userPhoto.getRootView(), "Friend Request Declined", Snackbar.LENGTH_SHORT).show();
                                holder.btnAccept.setVisibility(View.GONE);
                                holder.btnIgnore.setVisibility(View.GONE);
                                holder.requestText.setText(requests.get(position).getScreen_name()+"'s request declined!");
                            } else {
                                Snackbar.make(holder.userPhoto.getRootView(), "Internal Error. Please try again later!", Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void handleError(Exception e) throws Exception {
                            e.printStackTrace();
                        }
                    });
                }
            });
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
