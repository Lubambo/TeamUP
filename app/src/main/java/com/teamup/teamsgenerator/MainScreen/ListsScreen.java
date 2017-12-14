package com.teamup.teamsgenerator.MainScreen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.teamup.teamsgenerator.MainScreen.MatchListFrag.MatchesListFragment;
import com.teamup.teamsgenerator.MainScreen.PlayersListFrag.PlayersListFragment;
import com.teamup.teamsgenerator.MatchRegistrationScreen.MatchRegScreen;
import com.teamup.teamsgenerator.PlayerRegistrationScreen.PlayerRegScreen;
import com.teamup.teamsgenerator.R;

public class ListsScreen extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_screen);

        addBtn = (ImageButton) findViewById(R.id.tab_bar_add_button);

        assemblyToolbar();

    }

    private void assemblyToolbar(){

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new PlayersListFragment(), "Jogadores");
        viewPagerAdapter.addFragment(new MatchesListFragment(), "Partidas");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        addBtnClickHandler();

    }

    private void addBtnClickHandler(){

        addBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                int page = viewPager.getCurrentItem();

                if(page == 0){
                    Intent it = new Intent(ListsScreen.this, PlayerRegScreen.class);
                    startActivity(it);
                    finish();
                }
                else{
                    Intent it = new Intent(ListsScreen.this, MatchRegScreen.class);
                    startActivity(it);
                    finish();
                }

            }

        });

    }

}
