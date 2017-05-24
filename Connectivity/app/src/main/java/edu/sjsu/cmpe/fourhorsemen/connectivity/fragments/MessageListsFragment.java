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
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;

/**
 * Created by gauravchodwadia on 5/19/17.
 */

public class MessageListsFragment extends Fragment {
    private final static String TAG = PostFragment.class.getSimpleName();
    public final static int INBOX_LIST = 1;
    public final static int SENT_LIST = 2;

    private static final String PROFILE_ID = "profile-id";

    private OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    private static int messageListType = INBOX_LIST;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageListsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessageListsFragment newInstance(int type) {
        messageListType = type;
        MessageListsFragment fragment = new MessageListsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);

        Log.d("message list ----", "tryong to set adapter");
        // Set the adapter
        if (view instanceof RecyclerView) {

            Log.d("message list ----", "got instance of RecycleView");
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if(messageListType == 1){
                mAdapter = new MyMessageRecyclerViewAdapter(getMessages(getContext()).first, mListener);
            } else {
                mAdapter = new MyMessageRecyclerViewAdapter(getMessages(getContext()).second, mListener);
            }

            Log.d("message list ----", " recyclerView.setAdapter(mAdapter);");
            recyclerView.setAdapter(mAdapter);

        }
        return view;
    }


    private Pair<List<Message>, List<Message>> getMessages(Context context) {
        final List<Message> received = new ArrayList<Message>();
        final List<Message> sent = new ArrayList<Message>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("unique_id", PreferenceHandler.getAccessKey());


//        Fetch messages method
        RequestHandler.HTTPRequest(getContext(), ProjectProperties.METHOD_FETCH_MESSAGES, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws Exception {
                if(response.getInt("status_code") == 200) {
                    JSONArray sentMessages = response.getJSONObject("message").getJSONArray("from_messages");
                    Log.d("sentmsg", sentMessages.toString());
                    JSONArray receivedMessages = response.getJSONObject("message").getJSONArray("to_messages");
                    Log.d("receivedmsg", receivedMessages.toString());
                    for(int i  = 0; i < sentMessages.length(); i++) {
                        JSONObject currentObj = sentMessages.getJSONObject(i);
                        sent.add(new Message(currentObj.getInt("message_id"),
                                new Profile(),
                                new Profile(currentObj.getInt("to"),
                                        currentObj.getString("profile_pic"),
                                        currentObj.getString("screen_name")),
                                currentObj.getString("subject"),
                                currentObj.getString("message"),
                                currentObj.getString("timestamp")));
                    }
                    for(int i  = 0; i < receivedMessages.length(); i++) {
                        JSONObject currentObj = receivedMessages.getJSONObject(i);
                        received.add(new Message(currentObj.getInt("message_id"),
                                new Profile(currentObj.getInt("profile"),
                                        currentObj.getString("profile_pic"),
                                        currentObj.getString("screen_name")),
                                new Profile(),
                                currentObj.getString("subject"),
                                currentObj.getString("message"),
                                currentObj.getString("timestamp")));
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
        return new Pair<List<Message>, List<Message>>(received, sent);
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
