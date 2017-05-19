package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ViewPager viewPager;

    private Boolean isFabOpen = false;
    private FloatingActionButton fabMain,fabAddFriend,fabFollow;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

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

        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setTitle("Gaurav Chodwadia");
        int dummyProfileID = 1;


        viewPager = (ViewPager) root.findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
            adapter.addFrag(PostFragment.newInstanceForProfile(1, dummyProfileID), "My Posts");
            adapter.addFrag(new PostFragment(), "About");
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


        fabMain = (FloatingActionButton)root.findViewById(R.id.fabMain);
        fabAddFriend = (FloatingActionButton)root.findViewById(R.id.fabAddFriend);
        fabFollow = (FloatingActionButton)root.findViewById(R.id.fabFollow);
        fab_open = AnimationUtils.loadAnimation(root.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(root.getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(root.getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(root.getContext(),R.anim.rotate_backward);

        final boolean dummyIsFriend = false;
        final boolean dummyIsFollowing = false;

        View.OnClickListener fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.fabMain:
                        animateFAB();
                        break;
                    case R.id.fabAddFriend:
                        if(!dummyIsFriend) {
                            // TODO: Make this button to be "Unfriend" button.
                            // TODO: Make call to METHOD_ADD_FRIEND REST API
                        } else {
                            // TODO: Make this button to be "Add Friend" button.
                            // TODO: Make call to METHOD_UNFRIEND REST API
                        }
                        break;
                    case R.id.fabFollow:
                        if(!dummyIsFollowing) {
                            // TODO: Make this button to be "Unfollow" button.
                            // TODO: Make call to METHOD_FOLLOW REST API
                        } else {
                            // TODO: Make this button to be "Unfollow" button.
                            // TODO: Make call to METHOD_UNFOLLOW REST API
                        }
                        break;
                }
            }
        };

        fabMain.setOnClickListener(fabListener);
        fabAddFriend.setOnClickListener(fabListener);
        fabFollow.setOnClickListener(fabListener);

        return root;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.navigation, menu);
        super.onCreateOptionsMenu(menu,inflater);
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

    public void animateFAB(){

        if(isFabOpen){

            fabMain.startAnimation(rotate_backward);
            fabAddFriend.startAnimation(fab_close);
            fabFollow.startAnimation(fab_close);
            fabAddFriend.setClickable(false);
            fabFollow.setClickable(false);
            isFabOpen = false;
        } else {

            fabMain.startAnimation(rotate_forward);
            fabAddFriend.startAnimation(fab_open);
            fabFollow.startAnimation(fab_open);
            fabAddFriend.setClickable(true);
            fabFollow.setClickable(true);
            isFabOpen = true;
        }
    }
}
