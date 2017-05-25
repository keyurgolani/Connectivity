package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Message;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Profile;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Request;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;

/**
 * Created by gauravchodwadia on 5/19/17.
 */

public class FriendsListsFragment extends Fragment {
    private final static String TAG = PostFragment.class.getSimpleName();
    public final static int RECEIEVED_LIST = 1;
    public final static int SENT_LIST = 2;

    private static final String PROFILE_ID = "profile-id";

    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    private int friendsListType = RECEIEVED_LIST;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendsListsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FriendsListsFragment newInstance(int type) {
        FriendsListsFragment fragment = new FriendsListsFragment();
        fragment.friendsListType = type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if(friendsListType == 1){
                mAdapter = new MyFriendsRecyclerViewAdapter(getRequests(getContext()).second, mListener);
            } else {
                mAdapter = new MyFriendsRecyclerViewAdapter(getRequests(getContext()).first,mListener);
            }

            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }





    private Pair<List<Request>, List<Request>> getRequests(Context context) {
        final List<Request> received = new ArrayList<Request>();
        final List<Request> sent = new ArrayList<Request>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("unique_id", PreferenceHandler.getAccessKey());


        //sent request
        RequestHandler.HTTPRequest(getContext(), ProjectProperties.METHOD_GET_SENT_REQUEST, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(response.getInt("status_code") == 200) {
                    JSONArray sentRequest = response.getJSONArray("message");
                    for(int i  = 0; i < sentRequest.length(); i++) {
                        JSONObject currentObj = sentRequest.getJSONObject(i);

                        //Creating a request object
                        Request req_object = new Request();
                        req_object.setProfile(currentObj.getString("request_receiver"));
                        req_object.setScreen_name(currentObj.getString("name"));
                        req_object.setRequest_type("sent");

                        sent.add(req_object);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Internal Error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                e.printStackTrace();
            }
        });


        //received request
        RequestHandler.HTTPRequest(getContext(), ProjectProperties.METHOD_GET_RECEIVED_REQUEST, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(response.getInt("status_code") == 200) {
                     JSONArray receivedMessages = response.getJSONArray("message");

                    for(int i  = 0; i < receivedMessages.length(); i++) {
                        JSONObject currentObj = receivedMessages.getJSONObject(i);

                        //Creating a request object
                        Request req_object = new Request();
                        req_object.setProfile(currentObj.getString("request_sender"));
                        req_object.setScreen_name(currentObj.getString("name"));
                        req_object.setRequest_type("received");
                        received.add(req_object);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Internal Error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleError(Exception e) throws Exception {
                e.printStackTrace();
            }
        });



        return new Pair<List<Request>, List<Request>>(received, sent);
    }










    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction();

    }
}
