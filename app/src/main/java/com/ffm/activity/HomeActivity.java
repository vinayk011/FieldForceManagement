package com.ffm.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.ffm.FieldForceApplication;
import com.ffm.R;
import com.ffm.constants.IntentConstants;
import com.ffm.databinding.ActivityHomeBinding;
import com.ffm.listener.DialogListener;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.MyContextWrapper;
import com.ffm.util.Trace;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> implements BiometricCallback {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BiometricManager biometricManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, AppPreference.getInstance().getString(AppPrefConstants.PREF_LANGUAGE, "en")));
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        //loadLocale();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        drawerLayout = binding.drawerLayout;
        setDestination();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout).build();

        // Set up ActionBar
        setSupportActionBar(binding.toolbar);
        refreshActionBar();
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Set up navigation menu
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        updateNavHeader();

    }

    private void refreshActionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void updateNavHeader() {
        try {
            View headerView = binding.navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.user_no);
            navUsername.setText(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDestination() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_nav_fragment);  // Hostfragment
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.navigation_graph);
        graph.setDefaultArguments(getIntent().getExtras());
        if (false) {//!AppPreference.getInstance().getBoolean(AppPrefConstants.SIGN_IN)) {
            Trace.i("");
            // graph.setStartDestination(R.id.signin_fragment);
        } else {
            graph.setStartDestination(R.id.reports_fragment);
        }
        navHostFragment.getNavController().setGraph(graph);
        navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());
        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navHostFragment.getNavController());
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        return navController.navigateUp();
    }


    public void resume() {
        verifyBiometric();
    }

    private void verifyBiometric() {
        if (biometricManager == null) {
            biometricManager = new BiometricManager.BiometricBuilder(this)
                    .setTitle(getString(R.string.biometric_title))
                    .setSubtitle(getString(R.string.biometric_subtitle))
                    .setDescription(getString(R.string.biometric_description))
                    .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                    .build();
        }
        biometricManager.authenticate(this);
    }

    private void loadLocale() {
        setLocale(AppPreference.getInstance().getString(AppPrefConstants.PREF_LANGUAGE, "en"));
    }

    public void setLocale(String localeStr) {
        MyContextWrapper.wrap(getBaseContext(), localeStr);
        AppPreference.getInstance().putString(AppPrefConstants.PREF_LANGUAGE, localeStr);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (R.id.reports_fragment == getNavController().getCurrentDestination().getId()) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void setDrawerLocked(boolean shouldLock) {
        if (shouldLock) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        refreshActionBar();
    }*/

    public NavController getNavController() {
        if (navController == null)
            navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        return navController;
    }

    public void pause() {
        if (biometricManager != null) {
            biometricManager.dismissBiometricDialog();
        }
    }

    public void destroy() {

    }

    public void showCropAndUpload(String path) {

        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.EXTRA, path);
        getNavController().navigate(R.id.image_crop_fragment, bundle);
        /*if (!(fragmentStackHandler.getLastFragment() instanceof ImageCropFragment)) {
            Fragment fragment = new ImageCropFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.EXTRA, path);
            fragment.setArguments(bundle);
            fragmentStackHandler.startAndAddFragmentToStack(fragment, activityDashboardBinding.homeContainer.getId());
            activityDashboardBinding.optionsLayout.setVisibility(View.INVISIBLE);
            activityDashboardBinding.back.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onSdkVersionNotSupported() {
        //FieldForceApplication.getInstance().showToast(getString(R.string.biometric_error_sdk_not_supported));
        showAlert(getString(R.string.alert), getString(R.string.biometric_error_sdk_not_supported),
                false, () -> {
                    finish();
                });
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        showAlert(getString(R.string.alert), getString(R.string.biometric_error_hardware_not_supported),
                false, () -> {
                    finish();
                });
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        FieldForceApplication.getInstance().showToast(getString(R.string.biometric_error_fingerprint_not_available));
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        FieldForceApplication.getInstance().showToast(getString(R.string.biometric_error_permission_not_granted));
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        FieldForceApplication.getInstance().showToast(error);
    }

    @Override
    public void onAuthenticationFailed() {
        FieldForceApplication.getInstance().showToast(getString(R.string.biometric_failure));
    }

    @Override
    public void onAuthenticationCancelled() {
        showAlert(getString(R.string.alert), getString(R.string.biometric_cancelled),
                false, () -> {
                    finish();
                });
    }

    @Override
    public void onAuthenticationSuccessful() {
        FieldForceApplication.getInstance().showToast(getString(R.string.biometric_success));
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        FieldForceApplication.getInstance().showToast(helpString.toString());
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        //FieldForceApplication.getInstance().showToast(errString.toString());
        showAlert(getString(R.string.alert), errString.toString(),
                false, () -> {
                    finish();
                });
    }
}
