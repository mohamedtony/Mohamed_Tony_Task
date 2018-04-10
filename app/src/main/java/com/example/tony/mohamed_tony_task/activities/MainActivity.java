package com.example.tony.mohamed_tony_task.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tony.mohamed_tony_task.BroadCastRecevier.ChecNetworkReciever;
import com.example.tony.mohamed_tony_task.R;
import com.example.tony.mohamed_tony_task.adapters.UsersAdapter;
import com.example.tony.mohamed_tony_task.webServices.RetrofitWebService;
import com.example.tony.mohamed_tony_task.webServices.responses.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ChecNetworkReciever.OnNetworkChangeInterface, UsersAdapter.OnButtonsItemClicked {
    private static final int PERMISSION_CALL_REQUEST_CODE = 1;
    private static final int PERMISSION_SMS_REQUEST_CODE = 2;
    public static String string;
    private static RelativeLayout relativeLayout;
    private static String LIST_STATE_KEY = "save_list_state";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UsersAdapter usersAdapter;
    private ProgressBar progressBar;
    private ChecNetworkReciever checNetworkReciever;
    private List<User> usersList = new ArrayList<>();
    private int item_position;
    private Parcelable mListState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //================================= set custom toolbar ========================
        setToolbar();
        //================================= initialize views =========================
        initViews();

        //================================= to save users list and prevent loading again from net =========
        if (savedInstanceState != null && savedInstanceState.containsKey("users")) {
            usersList = savedInstanceState.getParcelableArrayList("users");
            if (usersList != null) {
                usersAdapter = new UsersAdapter(MainActivity.this, MainActivity.this, usersList, recyclerView);
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

        } else {
            if (checkConectivity()) {
                getUserDataProfile();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                dummyData();
                showNetworkSnakeBar(getString(R.string.network));
            }
        }

        //==================== reqister a broad cast reciever for receive the status of Network if changed ====
        checNetworkReciever = new ChecNetworkReciever(this);

    }

    //============================== dummy data for testing ======================
    private void dummyData() {
        User user = new User();
        user.setContactAddress(" cairo , cairo");
        user.setContactEmail("mohamedtony349@yahoo.com");
        user.setContactFax("90909090909");
        user.setContactMobile("01111046148");
        user.setContactPhone("01090885823");
        user.setContactId("22");
        user.setContactName(" Mohamed Tony tony");
        user.setContactSite("www.facebook.com");
        usersList.add(user);

        User user1 = new User();
        user1.setContactAddress(" cairo , cairo");
        user1.setContactEmail("mohamedtony349@yahoo.com");
        user1.setContactFax("90909090909");
        user1.setContactMobile("01111046148");
        user1.setContactPhone("01090885823");
        user1.setContactId("22");
        user1.setContactName(" Mohamed Tony ");
        user1.setContactSite("www.facebook.com");
        usersList.add(user1);

        User user2 = new User();
        user2.setContactAddress(" cairo , cairo");
        user2.setContactEmail("mohamedtony349@yahoo.com");
        user2.setContactFax("90909090909");
        user2.setContactMobile("01111046148");
        user2.setContactPhone("01090885823");
        user2.setContactId("22");
        user2.setContactName(" Mohamed Tony ");
        user2.setContactSite("www.facebook.com");
        usersList.add(user2);

        User user3 = new User();
        user3.setContactAddress(" cairo , cairo");
        user3.setContactEmail("mohamedtony349@yahoo.com");
        user3.setContactFax("90909090909");
        user3.setContactMobile("01111046148");
        user3.setContactPhone("01090885823");
        user3.setContactId("22");
        user3.setContactName(" Mohamed Tony ");
        user3.setContactSite("www.facebook.com");
        usersList.add(user3);

        User user4 = new User();
        user4.setContactAddress(" cairo , cairo");
        user4.setContactEmail("mohamedtony349@yahoo.com");
        user4.setContactFax("90909090909");
        user4.setContactMobile("01111046148");
        user4.setContactPhone("01090885823");
        user4.setContactId("22");
        user4.setContactName(" Mohamed Tony ");
        user4.setContactSite("www.facebook.com");
        usersList.add(user4);

        usersAdapter = new UsersAdapter(MainActivity.this, MainActivity.this, usersList, recyclerView);
        recyclerView.setAdapter(usersAdapter);

    }

    //============================= seting yout custom tool bar ======================
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setTitle(getString(R.string.toolbar_name));
        }
    }

    /*
     * To check if the user turned on the network or not
     * and if he turned on will be registered
     */
    @Override
    protected void onResume() {
        super.onResume();
        // getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

        //============================= to restore reycler view position =================
        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }

        //============================= register a broad cast reciever ===================
        registerReceiver(
                checNetworkReciever,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checNetworkReciever);
    }

    /*
     * save and restore the recycler view state
     */
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if (state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = layoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);

        if (usersList != null) {
            state.putParcelableArrayList("users", new ArrayList<User>(usersList));
        }
    }

    //=========================================== showing snakbar when ther is no network========================
    //===========================================================================================================
    private void showNetworkSnakeBar(final String snackbarString) {
        Snackbar.make(relativeLayout, snackbarString, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkConectivity()) {
                            getUserDataProfile();
                        } else {
                            showNetworkSnakeBar(snackbarString);
                        }
                    }
                }).setDuration(4000).show(); // Don’t forget to show!

    }
    //=========================================== showing top snakbar when ther is no network====================
    //===========================================================================================================
    public static void showTopSnakeBar(String contact_info) {
        Snackbar snackbar = Snackbar.make(relativeLayout, TextUtils.concat(contact_info + "\n", " ", spannableStringColor(string)), Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);  // show multiple line
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.setDuration(4000).show(); // Don’t forget to show!
    }

    //================================== for changing a part of string color in the snackbar====================
    private static SpannableString spannableStringColor(String string) {
        SpannableString spannableStringObject = new SpannableString(string);
        spannableStringObject.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableStringObject.length(), 0);
        //spannableStringObject.setSpan(new UnderlineSpan(), 0, spannableStringObject.length(), 0);
        return spannableStringObject;
    }


    //==================== to get users data from web services ===================
    //===================== using Retrofit========================================

    private void getUserDataProfile() {
        RetrofitWebService.getService().getUsersData().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                progressBar.setVisibility(View.INVISIBLE);
                usersList = response.body();
                usersAdapter = new UsersAdapter(MainActivity.this, MainActivity.this, usersList, recyclerView);
                recyclerView.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("error", t.getMessage());
                showNetworkSnakeBar(getString(R.string.somthing_wrong));
                progressBar.setVisibility(View.INVISIBLE);

                //========================== static data for testing only =============================
                dummyData();

            }
        });
    }


    //=================================================== checking network connectivity method ==========================
    //======================================================================================================
    private boolean checkConectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    //======================================= initializing the views ===========================================
    //==========================================================================================================
    private void initViews() {
        recyclerView = findViewById(R.id.usersRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        usersAdapter = new UsersAdapter(MainActivity.this, MainActivity.this, usersList, recyclerView);
        recyclerView.setAdapter(usersAdapter);
        relativeLayout = (RelativeLayout) findViewById(R.id.parent_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        string = getString(R.string.click_long);
    }

    @Override
    public void onChangedNetwork(boolean changed) {
        if (changed) {
            if (usersList.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                getUserDataProfile();
            }
        }
    }

    //============================ when call button is clicked =====================================
    @Override
    public void onCallClicked(int position) {
        item_position = position;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkCallPermission()) {
                makeClall(position);
            } else {
                requestCallPermission();
            }
        } else {
            makeClall(position);
        }

    }

    //=============================================== make an implicit intent for call================
    //================================================================================================
    private void makeClall(int position) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + usersList.get(position).getContactMobile()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    //============================ when send button is clicked =========================================
    @Override
    public void onSmsClicked(int position) {
        if (checkSmsPermission()) {
            makeSms(position);
        } else {
            requestSmsPermission();
        }
    }

    private void makeSms(int position) {
        Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + usersList.get(position).getContactMobile()));
        startActivity(it);
    }

    /*
     *  ========================================= to request run time call permision for marshemallaw and above verions==========================
     */

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_REQUEST_CODE);
        }
    }


    private boolean checkCallPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    /*
     *  ========================================= to request run time sms  permision for marshemallaw and above verions==========================
     */
    private boolean checkSmsPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void requestSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SMS_REQUEST_CODE);

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeClall(item_position);
                }
                break;
            case PERMISSION_SMS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeSms(item_position);
                }
                break;
        }
    }

}
