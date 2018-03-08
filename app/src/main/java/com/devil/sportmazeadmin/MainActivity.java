package com.devil.sportmazeadmin;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {


    private FirebaseUser user;
    private ViewPager mViewPager;

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        if(getSupportActionBar()!=null)
            setContentView(R.layout.activity_main);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        ((TextView)findViewById(R.id.user_name)).setText(user.getDisplayName());

        TabLayout tabLayout= findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if(getSupportActionBar()!=null) getSupportActionBar().hide();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new VideoList();
                case 1:
                    return new Upload();
                case 2:
                    return new ViewerList();
                default:
                    return null;
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
                    return "VIDEO LIST";
                case 1:
                    return "UPLOAD";
                case 2:
                    return "USERS";
            }
            return null;
        }
    }
}