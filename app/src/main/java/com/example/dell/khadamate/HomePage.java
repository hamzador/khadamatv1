package com.example.dell.khadamate;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.khadamate.Fragments.BestWorker;
import com.example.dell.khadamate.Fragments.HomePageFragment;
import com.example.dell.khadamate.Fragments.NormalUserProfile;

import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.user;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CardView mPlumberButton,mMechanicButton
            ,mElectricianButton,mCarpenterButton;
    TextView mNormalUserMail;
    Fragment fragment = null;

    public static LocationManager mLocationManager;
    public static MyLocationListener MyLoc;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.homePage,new HomePageFragment()).commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.FullName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.NormalUserMail);
        ImageView imageView  = (ImageView) headerView.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.ic_user_avatar);
        navUsername.setText(""+user.getFullName());
        navEmail.setText(""+user.getEmail());

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragment instanceof HomePageFragment)
            super.onBackPressed();
            else showHome();
        }
    }

    private void showHome(){
        fragment = new HomePageFragment();
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.homePage,fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
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
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            mAuth.signOut();
            user = null;
            this.startActivity(new Intent(HomePage.this,splashScreen.class));
            finish();
        } else if(id == R.id.nav_bestOf){
            fragment = new BestWorker();
        } else if (id == R.id.nav_homePage) {
            fragment = new HomePageFragment();
        } else if (id == R.id.nav_personal) {
            fragment = new NormalUserProfile();
        }/* else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.homePage,fragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
