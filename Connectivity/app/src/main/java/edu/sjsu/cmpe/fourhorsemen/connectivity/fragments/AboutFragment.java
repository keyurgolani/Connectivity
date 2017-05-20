package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.SettingsActivity;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.Utilities;

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
    Button btnEdit, btnSettings;
    EditText screen_name;
    EditText email_id;
    EditText location;
    EditText profession;
    EditText aboutme;
    EditText interests;
    Context context;
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
        setContext(view.getContext());
        screen_name = (EditText) view.findViewById(R.id.et_screen_name);
        email_id = (EditText) view.findViewById(R.id.et_emailid);
        location = (EditText) view.findViewById(R.id.et_loc);
        profession = (EditText) view.findViewById(R.id.et_profession);
        btnEdit = (Button) view.findViewById(R.id.btn_edit_profile);
        btnSettings = (Button) view.findViewById(R.id.btn_settings);

        View.OnClickListener btnListener = new View.OnClickListener() {

            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.btn_edit_profile:
                        if(btnEdit.getText().equals("Edit Details")){
                            changeToEditableText();
                        } else if (btnEdit.getText().equals("Save Details")) {
                            doEditProfile();
                        }
                        break;
                    case R.id.btn_settings:
                        Intent intent = new Intent(getContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        };
        btnEdit.setOnClickListener(btnListener);
        btnSettings.setOnClickListener(btnListener);
        aboutme = (EditText) view.findViewById(R.id.et_about);
        interests = (EditText) view.findViewById(R.id.et_interests);
        btnEdit = (Button)view.findViewById(R.id.btn_edit_profile);
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
                    JSONObject profile = response.getJSONArray("message").getJSONObject(0);
                    screen_name.setText(profile.getString("screen_name"));
                    email_id.setText("Not going to show email. Will be removed from here.");
                    location.setText(profile.getString("location"));
                    profession.setText(profile.getString("profession"));
                    aboutme.setText(profile.getString("about_me"));
                    interests.setText(profile.getString("interests"));
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

    public void doEditProfile(){
        btnEdit.setText("Edit Details");
        screen_name.setFocusableInTouchMode(false);
        email_id.setFocusableInTouchMode(false);
        location.setFocusableInTouchMode(false);
        profession.setFocusableInTouchMode(false);
        aboutme.setFocusableInTouchMode(false);
        interests.setFocusableInTouchMode(false);
        final String screenName = screen_name.getText().toString();
        final String emailId = email_id.getText().toString();
        final String locationStr = location.getText().toString();
        final String professionStr = profession.getText().toString();
        final String aboutMeStr = aboutme.getText().toString();
        final String interestStr = interests.getText().toString();
        btnEdit.setBackgroundColor(btnEdit.getContext().getResources().getColor(R.color.white));
        HashMap<String, String> params = new HashMap<String, String>();
        String unique_id = PreferenceHandler.getAccessKey();
        params.put("unique_id",unique_id);
        params.put("screen_name",screenName);
        params.put("email_id",emailId);
        params.put("location",locationStr);
        params.put("about_me",aboutMeStr);
        params.put("interest",interestStr);
        params.put("profession",professionStr);

        RequestHandler.HTTPRequest(getContext(), ProjectProperties.METHOD_UPDATE_PROFILE, params, new ResponseHandler() {
            @Override
            public void handleSuccess(JSONObject response) throws JSONException {
                if(response.getInt("status_code") == 200) {
                    //todo Add the response params retrieved in the set methods
                    screen_name.setText(screenName);
                    email_id.setText(emailId);
                    location.setText(locationStr);
                    profession.setText(professionStr);
                    aboutme.setText(aboutMeStr);
                    interests.setText(interestStr);
                    Utilities.cacheProfile(getContext());
                    Toast.makeText(getContext(), "Profile Updated.", Toast.LENGTH_LONG).show();
                } else if(response.getInt("status_code") == 403) {
                    Log.i("err","Forbidden");
                }
            }

            @Override
            public void handleError(Exception e) {
                e.printStackTrace();

            }
        });
    }

    public void changeToEditableText(){
        btnEdit.setBackgroundColor(btnEdit.getContext().getResources().getColor(R.color.colorPrimary));
        btnEdit.setText("Save Details");
        screen_name.setFocusableInTouchMode(true);
        email_id.setFocusableInTouchMode(true);
        location.setFocusableInTouchMode(true);
        profession.setFocusableInTouchMode(true);
        aboutme.setFocusableInTouchMode(true);
        interests.setFocusableInTouchMode(true);
    }

    public void setContext(Context context){
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }
}
