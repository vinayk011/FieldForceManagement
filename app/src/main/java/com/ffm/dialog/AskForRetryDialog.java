package com.ffm.dialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;


import com.ffm.R;
import com.ffm.databinding.DialogAskForRetryBinding;
import com.ffm.listener.ConfirmListener;
import com.ffm.network.AskForRetryListener;

import androidx.databinding.DataBindingUtil;


public class AskForRetryDialog extends BaseDialog {
    private DialogAskForRetryBinding dialogAskForRetryBinding;
    private AskForRetryListener askForRetryListener;

    public AskForRetryDialog(Context context, AskForRetryListener askForRetryListener) {
        super(context);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.askForRetryListener = askForRetryListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAskForRetryBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_ask_for_retry, null, false);
        setContentView(dialogAskForRetryBinding.getRoot());
        dialogAskForRetryBinding.setCallback(dialogListener);
        dialogAskForRetryBinding.setShow(!isConnected());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialogListener = null;
        dialogAskForRetryBinding = null;
    }

    private ConfirmListener dialogListener = new ConfirmListener() {
        @Override
        public void ok() {
            askForRetryListener.retry();
            dismiss();
        }

        @Override
        public void cancel() {
            askForRetryListener.cancel();
            dismiss();
        }
    };

    public boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}