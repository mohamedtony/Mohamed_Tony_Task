package com.example.tony.mohamed_tony_task.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.example.tony.mohamed_tony_task.R;
import com.example.tony.mohamed_tony_task.adapters.UsersAdapter;
import com.example.tony.mohamed_tony_task.webServices.responses.User;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int PERMISSION_CALL_REQUEST_CODE = 1;
    private User user;
    private TextView userName, userAddress, userMobile, userPhone, userFax, userWebSite, userEmaile;
    private CircleImageView userImageView;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //================================= to get the user object ===========================================
        //================================= from the intent ==================================================
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(UsersAdapter.USER_OBJECT)) {
            user = (User) getIntent().getParcelableExtra(UsersAdapter.USER_OBJECT);
        }

        //==================================== set the custom toolbar ========================================
        //====================================================================================================
        setToolbar();

        //====================================== initialize views ============================================
        initViews();
    }

    //=========================================s e custum toolbar method =====================================
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
            toolbar.setContentInsetsAbsolute(0, 0);
            //  toolbar.setContentInsetStartWithNavigation(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
            if (user != null) {
                toolbar.setTitle(user.getContactName());
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    /*
     * ===================== initializing views and set texts =====================
     */
    private void initViews() {

        userName = (TextView) findViewById(R.id.user_name);
        userAddress = (TextView) findViewById(R.id.user_address);
        userMobile = (TextView) findViewById(R.id.user_mobile);
        userPhone = (TextView) findViewById(R.id.user_phone);
        userFax = (TextView) findViewById(R.id.user_fax);
        userWebSite = (TextView) findViewById(R.id.user_site);
        userEmaile = (TextView) findViewById(R.id.user_email);
        userImageView = (CircleImageView) findViewById(R.id.user_circle_image);

        //=================================================== set text views ===============================================
        //==================================================================================================================

        if (user != null) {

            userName.setText(TextUtils.concat(spanStringsize("Name : "), " ", user.getContactName()));
            userEmaile.setText(TextUtils.concat(spanStringsize("Email : "), " ", spannableStringLine(user.getContactEmail())));
            userAddress.setText(TextUtils.concat(spanStringsize("Address : "), " ", user.getContactAddress()));
            if (!TextUtils.isEmpty(user.getContactFax())) {
                userFax.setText(TextUtils.concat(spanStringsize("Fax : "), " ", user.getContactFax()));
            } else {
                userFax.setVisibility(View.GONE);
            }
            userPhone.setText(TextUtils.concat(spanStringsize("Phone : "), " ", spannableStringLine(user.getContactPhone())));
            userMobile.setText(TextUtils.concat(spanStringsize("Mobile : "), " ", spannableStringLine(user.getContactMobile())));
            userWebSite.setText(TextUtils.concat(spanStringsize("WebSite : "), " ", spannableStringLine(user.getContactSite())));
        }
    }

    //============================================ when the colored blue text views clicked ====================================
    //==========================================================================================================================
    public void onTextViewClicked(View view) {
        if (view == userEmaile) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + user.getContactEmail())); // only email apps should handle this
            // intent.putExtra(Intent.EXTRA_EMAIL, /*user.getContactEmail()*/"mohamedtony349@yahoo.com");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (view == userMobile) {
            phoneNumber = user.getContactMobile();
            // phoneNumber="01090885823";
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkCallPermission()) {
                    makCall(phoneNumber);
                } else {
                    requestCallPermission();
                }
            } else {
                makCall(user.getContactMobile());
            }
        } else if (view == userPhone) {
            phoneNumber = user.getContactPhone();
            //phoneNumber="01111046148";
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (checkCallPermission()) {
                    makCall(phoneNumber);
                } else {
                    requestCallPermission();
                }
            } else {
                makCall(user.getContactMobile());
            }
        } else if (view == userWebSite) {
            openWebPage("https://" + user.getContactSite());
        }
    }

    //==============================================open browser with url ===========================================
    // ==============================================================================================================
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //===================================================make phone call ============================================
    //===============================================================================================================
    private void makCall(String contactMobile) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contactMobile));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
    /*
     *  ========================================= to request run time call permision for marshemallaw ==========================
     */

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_REQUEST_CODE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(this, "frommm GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    makCall(phoneNumber);


                } else {

                    // requestPermission();

                }
                break;
        }
    }

    /**
     * for giving size and color for word in the same text
     *
     * @param string
     * @return spannableString
     */
    private SpannableString spanStringsize(String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new RelativeSizeSpan(0.9f), 0, spannableString.length(), 0); // set size
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);// set color
        return spannableString;
    }

    private SpannableString spannableStringLine(String string) {
        SpannableString spannableStringObject = new SpannableString(string);
        spannableStringObject.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannableStringObject.length(), 0);
        spannableStringObject.setSpan(new UnderlineSpan(), 0, spannableStringObject.length(), 0);
        return spannableStringObject;
    }
}
