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
import android.widget.RelativeLayout;

import com.sketch.deliveryboy.R;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends Fragment {
    Fragment one, two, three;
    RelativeLayout rl_all,rl_viewed,rl_accepted;

    SectionPagerAdapter mSectionsPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, container, false);

        final TabLayout tabLayout =  view.findViewById(R.id.tab_layout);
        ViewPager viewPager =  view.findViewById(R.id.pager);
        rl_all =  view.findViewById(R.id.rl_all);
        rl_viewed =  view.findViewById(R.id.rl_viewed);
        rl_accepted =  view.findViewById(R.id.rl_accepted);


       /// setupViewPager(viewPager);


      /*  rl_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one = new Dashboard();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, one).commit();
            }
        });

        rl_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two = new Viewed_Dashboard();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, two).commit();
            }
        });


        rl_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three = new Accepted_Dashboard();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, three).commit();
            }
        });
*/

     tabLayout.setupWithViewPager(viewPager);

      //  viewPager.setCurrentItem(0, true);

        mSectionsPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.track_blue)
        );
        return view;
    }

   /* private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new All_Dashboard(), "ALL");
        adapter.addFrag(new Viewed_Dashboard(), "VIEWED");
        adapter.addFrag(new Accepted_Dashboard(), "ACCEPTED");

        viewPager.setAdapter(adapter);
    }
*/
   /* class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        private SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (one == null)
                        one = new All_Dashboard();
                    ((All_Dashboard)one).product_type_url();
                    return one;
                case 1:
                    if (two == null)
                        two = new Viewed_Dashboard();
                    return two;
                case 2:
                    if (three == null)
                        three = new Accepted_Dashboard();
                    return three;

                default:
                    one = new All_Dashboard();
            }
            return null;
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