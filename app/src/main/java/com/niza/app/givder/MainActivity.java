package com.niza.app.givder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.niza.app.givder.Utils.UiUtils;
import com.niza.app.givder.Utils.Utils;
import com.niza.app.givder.db.helpers.GivderContentDBHelper;
import com.niza.app.givder.fragments.ContentFragment;
import com.niza.app.givder.fragments.MatchesFragment;
import com.niza.app.givder.fragments.MyContributionsFragment;
import com.niza.app.givder.networking.actions.UserNetworkAction;

public class MainActivity extends AppCompatActivity {

    private View makePost;
    private ContentFragment contentFragment;
    private MatchesFragment matchesFragment;
    private MyContributionsFragment myContributionsFragment;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int loadCount;
    public void hideProgressBar(){
        loadCount++;
        if(loadCount>1){
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_main);

        makePost = findViewById(R.id.makePost);

        makePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewPostActivity.class);
                startActivityForResult(intent,0);
            }
        });
        findViewById(R.id.floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewPostActivity.class);
                startActivityForResult(intent,0);

            }
        });


          viewPager = (ViewPager) findViewById(R.id.viewpager);
         tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_whatshot_black_24dp).setText("FEED"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_star_black_24dp).setText("MATCHES"));


        // tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#9c27b0"));
       // tabLayout.setTabTextColors(Color.parseColor("#212121"), Color.parseColor("#9c27b0"));



        GivderContentDBHelper givderContentDBHelper= new GivderContentDBHelper(this);
        givderContentDBHelper.open();
        UserNetworkAction[] myuserNetworkActions = givderContentDBHelper.queryAll();
        givderContentDBHelper.close();
        if(myuserNetworkActions.length>0){
            findViewById(R.id.mainContent).setVisibility(View.VISIBLE);
            findViewById(R.id.asker).setVisibility(View.GONE);
            init();
        }
        else{
            findViewById(R.id.mainContent).setVisibility(View.GONE);
            findViewById(R.id.asker).setVisibility(View.VISIBLE);}


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }

    private void init(){
        loadCount = 0;
        hideProgressBar();
        loadCount = 0;
        viewPager.setAdapter(new Adapter());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }
    private class Adapter extends FragmentPagerAdapter {
        public Adapter() {
            super(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position==0){

                if(contentFragment==null)
                    contentFragment =  ContentFragment.newInstance(null);

                return contentFragment;
            }
            else if(position==1){

                if(matchesFragment==null)
                    matchesFragment = new MatchesFragment();
                return  matchesFragment;
            }
            else{

                if(myContributionsFragment==null)
                    myContributionsFragment = new MyContributionsFragment();
                return myContributionsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
