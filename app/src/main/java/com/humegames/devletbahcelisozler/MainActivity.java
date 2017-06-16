package com.humegames.devletbahcelisozler;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    tabFavoriler tabfavori = new tabFavoriler();
                    return tabfavori;
                case 1:
                    tabGiris tabgiris = new tabGiris();
                    return tabgiris;
                case 2:
                    tab1DilSursmeleri tab1 = new tab1DilSursmeleri();
                    return  tab1;
                case 3:
                    tab2Seslenisler tab2 = new tab2Seslenisler();
                    return  tab2;
                case 4:
                    tab3Seslenisler tab3 = new tab3Seslenisler();
                    return tab3;
                case 5:
                    tab4Seslenisler tab4 = new tab4Seslenisler();
                    return tab4;
                default:
                    return null;

            }


        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Favoriler";
                case 1:
                    return "Giris";
                case 2:
                    return "Dil Sürçmesi";
                case 3:
                    return "Sesleniş 1";
                case 4:
                    return "Sesleniş 2";
                case 5:
                    return "Sesleniş 3";

            }
            return null;
        }
    }
}
