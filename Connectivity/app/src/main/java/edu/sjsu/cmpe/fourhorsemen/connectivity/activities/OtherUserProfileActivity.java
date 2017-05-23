package edu.sjsu.cmpe.fourhorsemen.connectivity.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.AboutFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.ProfileFragment;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;

public class OtherUserProfileActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            ViewPagerAdapter adapter = new ViewPagerAdapter(this.getFragmentManager());
            adapter.addFrag(AboutFragment.newInstance(), "About");
            adapter.addFrag(PostFragment.newInstanceForProfile(1, Integer.parseInt(PreferenceHandler.getProfileID())), "My Posts");
            adapter.addFrag(new PostFragment(), "Albums");
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
