package com.ffm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ffm.activity.HomeActivity;
import com.ffm.dialog.LoadingDialog;
import com.ffm.permission.Permission;
import com.ffm.permission.PermissionCallback;
import com.ffm.permission.PermissionUtils;
import com.ffm.util.SnackbarHelper;
import com.ffm.util.Trace;
import com.google.android.material.snackbar.Snackbar;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public class BaseFragment<T extends ViewDataBinding> extends Fragment {
    public Context context;
    public Resources resources;
    public LoadingDialog loadingDialog;
    public T binding;

    private static final int PERMISSION_REQUEST_CODE = 27 << 1;
    private PermissionCallback permissionCallback;

    public void observeClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
        resources = getResources();
        context = getActivity();
        loadingDialog = new LoadingDialog(context, true);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            Set<String> keys = bundle.keySet();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IntentDump \n");
            stringBuilder.append("-------------------------------------------------------------\n");

            for (String key : keys) {
                stringBuilder.append(key).append("=").append(bundle.get(key)).append("\n");
            }

            stringBuilder.append("-------------------------------------------------------------\n");
            Trace.i(stringBuilder.toString());
        }
    }


    public void showLoading(String... message) {
        loadingDialog.show(message);
    }

    public void dismissLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    public void resume() {
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    public void pause() {
    }

    @Override
    public void onStart() {
        super.onStart();
        start();
    }

    public void start() {
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }

    public void stop() {
    }

    public void destroy() {
    }


    @Override
    public void onDestroy() {
        destroy();
        loadingDialog.dismiss();
        super.onDestroy();
        System.gc();
        Runtime.getRuntime().gc();
    }

    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void toastView(String msg) {
        hideKeyboard(context);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public Snackbar snackBarView(String msg) {
        hideKeyboard(context);
        Snackbar snackbar = Snackbar.make(((HomeActivity) context).findViewById(android.R.id.content), msg,
                Snackbar.LENGTH_LONG);
        SnackbarHelper.configSnackbar(context, snackbar);
        return snackbar;
    }

    public void requestPermission(Permission permission, PermissionCallback permissionCallback) {
        this.permissionCallback = permissionCallback;
        if (permissionCallback != null && !isRemoving()) {
            if (!PermissionUtils.isGranted(context, permission)) {
                requestPermissions(new String[]{permission.toString()}, PERMISSION_REQUEST_CODE);
            } else {
                this.permissionCallback.onPermissionResult(true, false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                permissionCallback.onPermissionResult(grantResults[i] == PackageManager.PERMISSION_GRANTED,
                        !PermissionUtils.shouldShowRequestPermissionRationale(getActivity(), permissions[i]));
            }
        }
    }

}
