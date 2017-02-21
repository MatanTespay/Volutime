package controller.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import controller.caldroid.CaldroidSampleActivity;
import controller.fragments.AdFragment;
import controller.fragments.AllOrgsDialogFragment;
import controller.fragments.EventFragment;
import controller.fragments.OrgProfileFragment;
import controller.fragments.OrganizationFragment;
import controller.fragments.MessagesFragment;
import controller.fragments.ProfileFragment;
import controller.fragments.PhotosFragment;
import model.ManagerDB;
import model.Organization;
import model.UserType;
import model.Volunteer;
import utils.RoundedImageView;
import utils.utilityClass;

import com.caldroidsample.R;

import static android.R.attr.handle;
import static android.R.attr.id;
import static android.R.id.icon2;
import static com.caldroidsample.R.id.fab;
import static com.caldroidsample.R.id.image;

public class MainActivity extends AppCompatActivity implements OrganizationFragment.OnFragmentInteractionListener,
         AllOrgsDialogFragment.OnVolAtOrgInteractionListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private UserType userType;
    private Organization org;
    private Volunteer vol;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_ORG = "ORG";
    private static final String TAG_CALENDER = "CALENDER";
    private static final String TAG_MESSAGES = "MESSAGES";
    private static final String TAG_PROFILE = "PROFILE";

    private static final String TAG_AD = "ad";
    private static final String TAG_AD_IMG_ID = "img_id";
    public static String CURRENT_TAG = TAG_ORG;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ManagerDB.getInstance().openDataBase(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        utilityClass.getInstance().setContext(getApplicationContext());
        utilityClass.getInstance().setFormatter();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if the Fragment is OrganizationFragment
                //Add organization to Volunteer


                //if the Fragment is OrgProfileFragment
                //Edit profile


                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        //set the initial navItemIndex according to userType
        if(getUserType() != null){
            if(getUserType().equals(UserType.volType)) {
                navItemIndex = 0;
            }else{
                navItemIndex = 2;
            }
        }


        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            if(getUserType() != null){
                if( getUserType().equals(UserType.volType)){
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_ORG;
                }
                else{
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_MESSAGES;
                }
            }

            loadHomeFragment();
        }
    }


    public FloatingActionButton getFab() {
        return fab;
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {

        //GET USER DATA AND TYPE
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            UserType type = (UserType) bd.get("userType");
            int id =  bd.getInt("userId");
            //set the activity user type
            setUserType(type);

            if(getUserType().equals(UserType.volType)){
                //get vol data
                this.vol = ManagerDB.getInstance().readVolunteer(id);
                // name, email
                txtName.setText(vol.getfName()+" "+vol.getlName());
                txtWebsite.setText(vol.getEmail());
                if(vol.getProfilePic() != null){
                    Bitmap roundBitmap = RoundedImageView.getCroppedBitmap(vol.getProfilePic(),300);
                    imgProfile.setImageBitmap(roundBitmap);
                }


            }
            else{
                //get org data
                navigationView.getMenu().getItem(0).setVisible(false);
                navigationView.getMenu().getItem(1).setVisible(false);
                this.org = ManagerDB.getInstance().readOrganization(id);
                txtName.setText(org.getName());
                txtWebsite.setText(org.getEmail());
                // set temp profile from drawable - need to patch from server
                if(org.getProfilePic() != null){
                    Bitmap roundBitmap = RoundedImageView.getCroppedBitmap(org.getProfilePic(),300);
                    imgProfile.setImageBitmap(roundBitmap);
                }

            }

        }




        //set image of nav menu background
        imgNavHeaderBg.setImageResource(R.drawable.nav_menu_header_bg);

        // TODO set this icon if new dates arrived
        // showing dot next to notifications label
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show fab with add_icon or hide button
            toggleFab(navItemIndex == 0 ,android.R.drawable.ic_input_add );
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                // configure link

                Bundle bundle = new Bundle();
                if(CURRENT_TAG.equals("ORG"))
                    bundle.putInt(TAG_AD_IMG_ID , R.drawable.ad_yad);
                else if(CURRENT_TAG.equals("photos"))
                    bundle.putInt(TAG_AD_IMG_ID , R.drawable.ad_ruach);
                else
                    bundle.putInt(TAG_AD_IMG_ID , R.drawable.ad_dog_volu);

                Fragment fragment = getCurrentFragment();
                Fragment fragmentAd = new AdFragment();

                fragmentAd.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.replace(R.id.adframe, fragmentAd, TAG_AD);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show fab button with add icon or hide
        toggleFab(navItemIndex == 0 , android.R.drawable.ic_input_add);

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    /**
     * get the current Fragment by navItemIndex
     * @return Fragment
     */
    private Fragment getCurrentFragment() {
        switch (navItemIndex) {
            case 0:
                // home

                OrganizationFragment organizationFragment = new OrganizationFragment();
                Bundle args1 = new Bundle();
                if(getVol()!=null){
                    args1.putInt("volID", this.getVol().getId());
                }
                organizationFragment.setArguments(args1);
                return organizationFragment;
            case 1:
                // photos -- cal
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 2:
                // fragment -- msg
                MessagesFragment messagesFragment = new MessagesFragment();
                return messagesFragment;
            case 3:
                // notifications fragment --
                //check the profile to send back
                Fragment frag;
                Bundle args = new Bundle();
                if(getOrg() != null){
                    // user is org.
                    frag = new OrgProfileFragment();
                    args.putInt("orgID", this.org.getId());
                }else {
                    // user is vol
                    frag = new ProfileFragment();
                    args.putInt("volID", this.vol.getId());
                }
                frag.setArguments(args);
                return frag;

/*            case 4:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;*/
            default:
                return new OrganizationFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    // set the select item of navigation
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_org:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_ORG;
                        break;
                    case R.id.nav_calendar:
                        //navItemIndex = 1;
                        //CURRENT_TAG = TAG_CALENDER;
                        Intent cal = new Intent(new Intent(MainActivity.this, CaldroidSampleActivity.class));
                        if(getVol() != null){
                            cal.putExtra("userID",getVol().getId());
                            startActivity(cal);
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_messages:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MESSAGES;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if(getUserType() != null){
                if(getUserType().equals(UserType.volType)) {
                    //home fragment for volunteer
                    if (navItemIndex != 0) {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_ORG;
                        loadHomeFragment();
                        return;
                    }
                }else{
                    //home fragment for org
                    if (navItemIndex != 2) {
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MESSAGES;
                        loadHomeFragment();
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is messages, load the menu created for notifications
        if (navItemIndex == 2) {
            getMenuInflater().inflate(R.menu.messages, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab

    /**
     * Method checks if show fab or not and which icon to show if state true
     * @param state
     * @param imageCode
     */
    public void toggleFab(boolean state ,int imageCode) {
        if (state ){
            fab.show();
            //android.R.drawable.ic_input_add

            fab.setImageDrawable(ContextCompat.getDrawable(this,imageCode));
        }
        else
            fab.hide();
    }

    /**
     */
    public void toggleFabListiner(View.OnClickListener handler) {
        fab.setOnClickListener(handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        selectNavMenu();
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {


    }

    /**
     * implement interface from AllOrg...
     * @param bundle args to send to activity.
     */
    @Override
    public void onVolAtOrgAction(Bundle bundle) {

        if(bundle != null){
            //get reference to frag.
            Fragment orgsFrag = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
            if(orgsFrag != null){
                ((OrganizationFragment)orgsFrag).loadDataToAdapter();
            }
        }
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    public Volunteer getVol() {
        return vol;
    }

    public void setVol(Volunteer vol) {
        this.vol = vol;
    }
}
