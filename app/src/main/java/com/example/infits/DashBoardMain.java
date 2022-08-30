package com.example.infits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DashBoardMain extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        nav = (NavigationView) findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (!DataFromDatabase.proUser){
            Menu nav_Menu = nav.getMenu();
            nav_Menu.findItem(R.id.consul).setVisible(false);
            nav_Menu.findItem(R.id.message).setVisible(false);
            nav_Menu.findItem(R.id.live).setVisible(false);
            nav_Menu.findItem(R.id.scan).setVisible(false);
        }

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.dboard:
                        Intent idb = new Intent(DashBoardMain.this, DashBoardMain.class);
                        startActivity(idb);
                        //Toast.makeText(getApplicationContext(),"Dashboard", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;

                    case R.id.charts:
                        Intent ich = new Intent(DashBoardMain.this, Statistics.class);
                        startActivity(ich);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.live:
                        Intent icL = new Intent(DashBoardMain.this, LiveListAct.class);
                        startActivity(icL);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.consul:
                        Intent icl = new Intent(DashBoardMain.this, Consultation.class);
                        startActivity(icl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.setting:
                        Intent ist = new Intent(DashBoardMain.this, Settings.class);
                        startActivity(ist);
                        //Toast.makeText(getApplicationContext(),"Settings", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.message:
                        Intent imt = new Intent(DashBoardMain.this, ChatArea.class);
                        startActivity(imt);
                        //Toast.makeText(getApplicationContext(),"Settings", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.scan:
                        Intent iscan = new Intent(DashBoardMain.this,ScanActivity.class);
                        startActivity(iscan);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("Weight",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("weight", "0");
        myEdit.putString("weightChangeDate", "");

        myEdit.apply();
    }
}