package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    /*@Bind(R.id. et_screen_name) EditText screen_name;
    @Bind(R.id. et_emailid) EditText email_id;
    @Bind(R.id. et_loc) EditText location;
    @Bind(R.id. et_profession) EditText profession;
    @Bind(R.id. et_about) EditText aboutme;
    @Bind(R.id. et_interests) EditText interests;
   */
    EditText screen_name;
    EditText email_id;
    EditText location;
    EditText profession;
    EditText aboutme;
    EditText interests;

    private OnFragmentInteractionListener mListener;

    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("tag","onCreate");
        //ButterKnife.bind(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_about, container, false);
        screen_name = (EditText) view.findViewById(R.id.et_screen_name);
        email_id = (EditText) view.findViewById(R.id.et_emailid);
        location = (EditText) view.findViewById(R.id.et_loc);
        profession = (EditText) view.findViewById(R.id.et_profession);
        aboutme = (EditText) view.findViewById(R.id.et_about);
        interests = (EditText) view.findViewById(R.id.et_interests);

        doGetProfileDetails(view.getContext());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void doGetProfileDetails(final Context context){
        HashMap<String, String> params = new HashMap<String, String>();
        String unique_id = PreferenceHandler.getAccessKey();
        params.put("unique_id",unique_id);
        RequestHandler.HTTPRequest(context, ProjectProperties.METHOD_FETCH_PROFILE, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws JSONException {
                if(response.getInt("status_code") == 200) {
                    //toDo: Add the response parameters
                    screen_name.setText("");
                    email_id.setText("");
                    location.setText("");
                    profession.setText("");
                    aboutme.setText("");
                    interests.setText("");
                }
            }

            @Override
            public void handleError(Exception e) {
                e.printStackTrace();

            }
        });
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
