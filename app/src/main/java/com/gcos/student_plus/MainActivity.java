package com.gcos.student_plus;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SP_NAME = "userDetails";

    TextView tv_Name,tv_Dname;
    UserLocalStore userLocalStore;
    private static long back_pressed_time;
    private static long PERIOD = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_Name = (TextView)findViewById(R.id.tv_Name);
        tv_Dname = (TextView)findViewById(R.id.tv_Dname);

        userLocalStore = new UserLocalStore(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame,new Community()).commit();

        //Call SharedPreferences And Assign to Attribute
        SharedPreferences prefs = getSharedPreferences(SP_NAME, 0);
        String userName = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        tv_Name.setText(userName);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed_time + PERIOD > System.currentTimeMillis()) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
                back_pressed_time = System.currentTimeMillis();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_community) {

            fm.beginTransaction().replace(R.id.content_frame,new Community()).commit();

        } else if (id == R.id.nav_homework) {

            fm.beginTransaction().replace(R.id.content_frame,new Homework()).commit();

        } else if (id == R.id.nav_event) {

            fm.beginTransaction().replace(R.id.content_frame,new Event()).commit();

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);

            startActivity(new Intent(this, Login.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    public void displayUseDetails(){

        User user = userLocalStore.getLoggedInUser();

    }

    @Override
    protected void onStart(){

        super.onStart();

        if (authenticate() == true){
            displayUseDetails();
        }

        else {

            startActivity(new Intent(MainActivity.this, Login.class));

        }

    }

}
