package sourabh.quotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;

import sourabh.quotes.app.Const;
import sourabh.quotes.app.CustomRequest;
import sourabh.quotes.helper.JsonSeparator;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AuthorsFragment.OnFragmentInteractionListener {

    DrawerLayout drawer;
    Context context;
    NavigationView rightNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView leftNavigationView = (NavigationView) findViewById(R.id.nav_left_view);
       // leftNavigationView.setNavigationItemSelectedListener(this);

        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Left navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {
//                    Intent i = new Intent(getApplicationContext(), AuthorsActivity.class);
//                    startActivity(i);
                    Fragment fragment = null; fragment = new AuthorsFragment();  //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }
                  //  Toast.makeText(HomeActivity.this, "Left Drawer - Import", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_authors) {
                    Toast.makeText(HomeActivity.this, "Left Drawer - Gallery", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_slideshow) {
                    Toast.makeText(HomeActivity.this, "Left Drawer - Slideshow", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_manage) {
                    Toast.makeText(HomeActivity.this, "Left Drawer - Tools", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_share) {
                    Toast.makeText(HomeActivity.this, "Left Drawer - Share", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_send) {
                    Toast.makeText(HomeActivity.this, "Left Drawer - Send", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);



        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                Toast.makeText(HomeActivity.this,""+ id, Toast.LENGTH_SHORT).show();


                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });




            context = getApplicationContext();

        getCategories();




    }



    public void getCategories()
    {
        HashMap hashmap = new HashMap();
        HashMap hashmap1 = new HashMap();
        hashmap1.put("Authorization", Const.GUEST_API_KEY);
        Volley.newRequestQueue(this).add(new CustomRequest(getApplicationContext(),this,
                false, 0,Const.URL_GET_CATEGORIES, hashmap, hashmap1,


                new com.android.volley.Response.Listener() {

            @Override
            public void onResponse(Object response) {
                JSONObject jsonObject = (JSONObject) response;
                JsonSeparator js= new JsonSeparator(context,jsonObject);

                try {
                    if(js.isError()){

                        Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }else{

                        JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);


                        final Menu menu = rightNavigationView.getMenu();

                        for (int i = 0 ; i< categories.length(); i++)
                        {
                            JSONObject single_category = categories.getJSONObject(i);

//                            menu.add(single_category.getString(Const.KEY_CATEGORY_NAME)).setIcon(getDrawable(R.drawable.ic_menu_gallery));

                            menu.add(
                                    0,
                                    single_category.getInt(Const.KEY_ID_CATEGORY),
                                    Menu.NONE,
                                    single_category.getString(Const.KEY_CATEGORY_NAME))
                                    .setIcon(getDrawable(R.drawable.ic_menu_send));


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }



    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


//    protected void displaySnackbar (Context context, View view, String s)
//    {
//        Snackbar snack = Snackbar.make(view, s, Snackbar.LENGTH_LONG);
//        View sbview = snack.getView();
//        sbview.setBackgroundColor(context.getColor(this, R.color.colorAccent));
//        TextView textView = (TextView) sbview.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(context.getResources().getColor(R.color.primary_light));
//        snack.show();
//    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_authors) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
