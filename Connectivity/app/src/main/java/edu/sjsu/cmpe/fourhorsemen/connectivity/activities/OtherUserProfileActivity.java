package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.AboutFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.ProfileFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;

public class OtherUserProfileActivity extends AppCompatActivity implements
        AboutFragment.OnFragmentInteractionListener,
        PostFragment.OnListFragmentInteractionListener {

    private ViewPager viewPager;
    private AboutFragment aboutFragment;
    private PostFragment postFragment;
    static int profileID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        profileID = getIntent().getIntExtra("profile", profileID);

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
            adapter.addFrag(AboutFragment.newInstanceForProfile(profileID), "About");
            adapter.addFrag(PostFragment.newInstanceForProfile(1, profileID), "My Posts");
            adapter.addFrag(PostFragment.newInstanceForProfile(1, profileID), "Albums");
            viewPager.setAdapter(adapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction() {

    }

    public void setProfileImage(String profile_pic) {
        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        byte[] decodedString = Base64.decode(profile_pic, Base64.DEFAULT);
        profilePic.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
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



}
