package controller.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caldroidsample.R;

import java.util.ArrayList;

import controller.caldroid.CaldroidSampleActivity;
import controller.fragments.AdFragment;
import controller.fragments.AllOrgsDialogFragment;
import controller.fragments.MessagesFragment;
import controller.fragments.OrgProfileFragment;
import controller.fragments.OrganizationFragment;
import controller.fragments.PhotosFragment;
import controller.fragments.ProfileFragment;
import model.ManagerDB;
import model.Organization;
import model.UserType;
import model.Volunteer;
import network.utils.NetworkConnector;
import network.utils.NetworkResListener;
import network.utils.ResStatus;
import utils.RoundedImageView;
import utils.utilityClass;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements OrganizationFragment.OnFragmentInteractionListener,
         AllOrgsDialogFragment.OnVolAtOrgInteractionListener, NetworkResListener {

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
    private ProgressDialog progressDialog = null;
    // if the db needs to update from server
    private boolean doVolGet = true;
    private boolean doOrgGet = true;
    private boolean doMsgGet = true;
    private boolean doEventGet = true;

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
    Bundle savedInstanceState;
    private int userId = 0;

    /**
     * continue to load activity after getting server data of vol
     * @param savedInstanceState
     */
    private void loadApp(Bundle savedInstanceState){

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        userId = 0;
        if(bd != null)
        {
            UserType type = (UserType) bd.get("userType");
            userId =  bd.getInt("userId");
            //set the activity user type
            setUserType(type);

        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        //the drawer at the corner
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // the icon at the right down corner
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

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

        if (this.savedInstanceState == null) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        ManagerDB.getInstance().openDataBase(this);
       /* ArrayList<Volunteer> result = new ArrayList<Volunteer>();
        result = ManagerDB.getInstance().readAllVolunteers();*/

        utilityClass.getInstance().setContext(getApplicationContext());
        utilityClass.getInstance().setFormatter();
        NetworkConnector.getInstance().setContext(this);

        loadDataFromServer(8); // volunteers

        setContentView(R.layout.activity_main);
    }

    /**
     *
     * @param resource
     */
    @Override
    public void onPreUpdate(String resource) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating resources : " + resource);
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * method get req. and returns result from server
     * @param req
     */
    public void loadDataFromServer(int req){

        if(req == 8){
            //sync volunteers
            NetworkConnector.getInstance().registerListener(this);
            NetworkConnector.getInstance().getVolunteers();
        }
        else if(req == 9){
            NetworkConnector.getInstance().registerListener(this);
            NetworkConnector.getInstance().getOrganizations();
        }
        else if(req == 10){
            NetworkConnector.getInstance().registerListener(this);
            NetworkConnector.getInstance().getVolevents();
        }
    }

    /**
     *
     * @param  res  - the data
     * @param status - the status of the update process
     * @param type
     */
    @Override
    public void onPostUpdate(byte[] res, ResStatus status, String type) {


        NetworkConnector.getInstance().unregisterListener(this);

        if(status == ResStatus.SUCCESS){
            if(type.equals("8")) { //volunteers
                String s = getResources().getString(R.string.vols);
                utilityClass.getInstance().showToast(R.string.preUpdate,1,s);
                ManagerDB.getInstance().updateVolunteers(res);
                doVolGet = false;
                progressDialog.dismiss();
                loadApp(this.savedInstanceState);
            }
            else if(type.equals("9")){ // organizations
                String s = getResources().getString(R.string.orgs);
                utilityClass.getInstance().showToast(R.string.preUpdate,1,s);
                ManagerDB.getInstance().updateOrganization(res);
                doOrgGet = false;
                progressDialog.dismiss();
            }
            else if(type.equals("10")){ //events
                String s = getResources().getString(R.string.events);
                utilityClass.getInstance().showToast(R.string.preUpdate,1,s);
                doEventGet = false;
                progressDialog.dismiss();
                ManagerDB.getInstance().updatVolEvent(res);
            }

        }
        else{
            Toast.makeText(this,"download failed...",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * get the fab
     * @return
     */
    public FloatingActionButton getFab() {
        return fab;
    }

    /**
     * set a new fab
     * @param fab
     */
    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {

        //GET USER DATA AND TYPE FROM DB

        //CHECKS USER TYPE
            if(getUserType().equals(UserType.volType)){
                //IF THE USER IS VOLUNTEER
                this.vol = ManagerDB.getInstance().readVolunteer(this.userId);
                if(this.vol != null){
                    txtName.setText(vol.getfName()+" "+vol.getlName());
                    txtWebsite.setText(vol.getEmail());
                    if(vol.getProfilePic() != null){
                        Bitmap roundBitmap = RoundedImageView.getCroppedBitmap(vol.getProfilePic(),300);
                        imgProfile.setImageBitmap(roundBitmap);
                    }
                }

            }
            else{
                // IF THE USRR IS ORG
                if(this.navigationView != null){
                    navigationView.getMenu().getItem(0).setVisible(false);
                    navigationView.getMenu().getItem(1).setVisible(false);
                }
                //get the organization data from db
                this.org = ManagerDB.getInstance().readOrganization(this.userId);
                if(this.org != null){
                    txtName.setText(org.getName());
                    txtWebsite.setText(org.getEmail());
                    // set temp profile from drawable - need to patch from server
                    if(org.getProfilePic() != null){
                        Bitmap roundBitmap = RoundedImageView.getCroppedBitmap(org.getProfilePic(),300);
                        imgProfile.setImageBitmap(roundBitmap);
                    }
                }
            }

        //}

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
     * shows the right frag. on the fragment layout acording to index
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
                Bundle args2 = new Bundle();
                if(getOrg() != null){
                    args2.putInt("orgID", this.org.getId());
                    args2.putSerializable("userType",userType.orgType);
                }else if(getVol() != null) {
                    args2.putInt("volID", this.vol.getId());
                    args2.putSerializable("userType",userType.volType);
                }
                messagesFragment.setArguments(args2);
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
                    frag.setArguments(args);
                    return frag;
                }else if(getVol() != null) {
                    // user is vol
                    frag = new ProfileFragment();
                    args.putInt("volID", this.vol.getId());
                    frag.setArguments(args);
                    return frag;
                }

/*            case 4:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;*/
            default:
                return new OrganizationFragment();
        }
    }

    /**
     * set the setToolbarTitle
     */
    private void setToolbarTitle() {
        if(getSupportActionBar() != null)
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    /**
     *
     */
    private void selectNavMenu() {
        if(navigationView != null)
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    /**
     *  set the select item of navigation
     */
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
                            cal.putExtra("doEventGet", doEventGet);
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

    /**
     * on click at back button the drawer will return to the first index
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        boolean checkExit = false;
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
                    else{
                        checkExit = true;
                        //check if viewing orgProfile and switch back to organizations fragment
                        Fragment orgProfileFrag = getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.org_profile));
                        if(orgProfileFrag != null && orgProfileFrag.isVisible()){
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_ORG;
                            loadHomeFragment();
                            return;
                        }
                    }
                }else{
                    //home fragment for org
                    if (navItemIndex != 2) {
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MESSAGES;
                        loadHomeFragment();
                        return;
                    }else{
                        checkExit = true;
                    }
                }
            }
        }

        if(checkExit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


        //super.onBackPressed();
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is messages, load the menu created for notifications
        if (navItemIndex == 2) {
            getMenuInflater().inflate(R.menu.messages, menu);
        }
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            finish();
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
        ManagerDB.getInstance().closeDataBase();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ManagerDB.getInstance().openDataBase(this);
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
// getters and setters
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

    public boolean isDoVolGet() {
        return doVolGet;
    }

    public void setDoVolGet(boolean doVolGet) {
        this.doVolGet = doVolGet;
    }

    public boolean isDoOrgGet() {
        return doOrgGet;
    }

    public void setDoOrgGet(boolean doOrgGet) {
        this.doOrgGet = doOrgGet;
    }

    public boolean isDoMsgGet() {
        return doMsgGet;
    }

    public void setDoMsgGet(boolean doMsgGet) {
        this.doMsgGet = doMsgGet;
    }

    public boolean isDoEventGet() {
        return doEventGet;
    }

    public void setDoEventGet(boolean doEventGet) {
        this.doEventGet = doEventGet;
    }
}
