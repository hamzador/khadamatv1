package com.example.dell.khadamate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.dell.khadamate.Fragments.WorkerHomePageFragment;
import com.example.dell.khadamate.Fragments.workerProfile;
import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.user;

public class WorkerHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.workerHomePage,new WorkerHomePageFragment()).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_worker);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.workerFullName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.WorkerMail);
        ImageView imageView  = (ImageView) headerView.findViewById(R.id.workerImageView);
        imageView.setImageResource(R.drawable.ic_user_avatar);
        navUsername.setText(""+user.getFullName());
        navEmail.setText(""+user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragment instanceof WorkerHomePageFragment)
                super.onBackPressed();
            else showHome();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.worker_home_page, menu);
        return true;
    }

    private void showHome(){
        fragment = new WorkerHomePageFragment();
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.workerHomePage,fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
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

        if (id == R.id.nav_worker_homePage) {
            fragment = new WorkerHomePageFragment();
            // Handle the camera action
        } else if (id == R.id.nav_worker_personal) {
            fragment = new workerProfile();
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        if(id == R.id.nav_worker_logout){
            mAuth.signOut();
            user = null;
            this.startActivity(new Intent(WorkerHomePage.this,splashScreen.class));
            finish();
        }

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.workerHomePage,fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
