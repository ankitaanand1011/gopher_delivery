package com.sketch.deliveryboy.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sketch.deliveryboy.R;




public class Dashboard extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, container, false);

        TabLayout tabLayout =  view.findViewById(R.id.tab_layout);
        ViewPager viewPager =  view.findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.track_blue)
        );
        return view;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        private SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new All_Dashboard();
                case 1:
                    return new Viewed_Dashboard();
                case 2:
                    return new Accepted_Dashboard();


                default:
                    return new All_Dashboard();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Viewed";
                case 2:
                    return "Accepted";
                default:
                    return "All";
            }
        }
    }
}