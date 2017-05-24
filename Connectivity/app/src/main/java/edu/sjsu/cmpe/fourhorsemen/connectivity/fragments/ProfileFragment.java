package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.MainActivity;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ProjectProperties;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.RequestHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.ResponseHandler;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.Utilities;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    static final String TAG = ProfileFragment.class.getSimpleName();
    static final int RESULT_PHOTO_FROM_GALLARY = 501;
    static final int RESULT_PHOTO_FROM_CAMERA = 502;

    private OnFragmentInteractionListener mListener;
    ImageView profilePic;

    private ViewPager viewPager;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.mToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        final CollapsingToolbarLayout ctbl = (CollapsingToolbarLayout) root.findViewById(R.id.ctbl);
//        AppBarLayout appBarLayout = (AppBarLayout) root.findViewById(R.id.appBar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    toolbar.hideOverflowMenu();
//                    isShow = false;
//                } else if(isShow) {
//
//                }
//            }
//        });



        profilePic = (ImageView) root.findViewById(R.id.profile_pic);

        byte[] decodedString = Base64.decode(PreferenceHandler.getProfilePic(), Base64.DEFAULT);
        profilePic.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setTitle(PreferenceHandler.getScreenName());


        viewPager = (ViewPager) root.findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
            adapter.addFrag(AboutFragment.newInstance(), "About");
            adapter.addFrag(PostFragment.newInstanceForProfile(1, Integer.parseInt(PreferenceHandler.getProfileID())), "My Posts");
            adapter.addFrag(new PostFragment(), "Albums");
            viewPager.setAdapter(adapter);
        }

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.pp_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, RESULT_PHOTO_FROM_CAMERA);
                return true;
            case R.id.pp_gallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , RESULT_PHOTO_FROM_GALLARY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.upload_profile_pic_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case RESULT_PHOTO_FROM_CAMERA:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profilePic.setImageURI(selectedImage);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("unique_id", PreferenceHandler.getAccessKey());
                    params.put("profile_pic", Utilities.encodePhoto(getContext(), selectedImage));
                    RequestHandler.HTTPRequest(getContext(), ProjectProperties.METHOD_UPDATE_PROFILE, params, new ResponseHandler() {
                        @Override
                        public void handleSuccess(JSONObject response) throws Exception {
                            Snackbar.make(getView(), "Photo Updated Successfully", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleError(Exception e) throws Exception {

                        }
                    });
                    //TODO: Code to send new photo to the database
                }

                break;
            case RESULT_PHOTO_FROM_GALLARY:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profilePic.setImageURI(selectedImage);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("unique_id", PreferenceHandler.getAccessKey());
                    params.put("profile_pic", Utilities.encodePhoto(getContext(), selectedImage));
                    RequestHandler.HTTPRequest(getContext(), ProjectProperties.METHOD_UPDATE_PROFILE, params, new ResponseHandler() {
                        @Override
                        public void handleSuccess(JSONObject response) throws Exception {
                            Snackbar.make(getView(), "Photo Updated Successfully", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleError(Exception e) throws Exception {

                        }
                    });
                    //TODO: Code to send new photo to the database

                }
                break;
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
