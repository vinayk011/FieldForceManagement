package com.ffm.permission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.ffm.R;
import com.ffm.databinding.DialogAskForPermissionBinding;
import com.ffm.dialog.BaseDialog;
import com.ffm.listener.ConfirmListener;

import androidx.databinding.DataBindingUtil;

public class AskForPermissionDialog extends BaseDialog<DialogAskForPermissionBinding> {
    private Context context;
    private String text;
    private boolean neverAsk;
    private AskForPermissionListener askForPermissionListener;

    public AskForPermissionDialog(Context context, String text, boolean neverAsk, AskForPermissionListener askForPermissionListener) {
        super(context);
        this.context = context;
        this.text = text;
        this.neverAsk = neverAsk;
        this.askForPermissionListener = askForPermissionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_ask_for_permission, null, false);
        setContentView(binding.getRoot());
        binding.setButton(neverAsk ? context.getString(R.string.settings) : context.getString(R.string.request));
        binding.setCallback(dialogListener);
        binding.setError(text);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialogListener = null;
        binding = null;
    }

    private ConfirmListener dialogListener = new ConfirmListener() {
        @Override
        public void ok() {
            if (neverAsk) {
                PermissionUtils.openApplicationSettings(context);
            } else {
                askForPermissionListener.ask();
            }
            dismiss();
        }

        @Override
        public void cancel() {
            askForPermissionListener.deny();
            dismiss();
        }
    };
}