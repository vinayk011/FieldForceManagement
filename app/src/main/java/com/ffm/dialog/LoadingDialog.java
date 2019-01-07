package com.ffm.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;


import com.ffm.R;
import com.ffm.databinding.DialogLoadingBinding;
import com.ffm.util.StringUtil;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Raviteja on 21-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {
    private String[] message;
    private AnimationDrawable myAnimationDrawable;

    public LoadingDialog(Context context, boolean cancelable, String... message) {
        super(context, R.style.fullScreenDialog);
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(cancelable);
        this.message = message;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setDimAmount(0.8f);
        }
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_loading, null, false);
        setContentView(binding.getRoot());
        setMessage(message);
    }

    public void show(String... message) {
        super.show();
        setMessage(message);
    }

    private void setMessage(String... message) {
        if (message.length > 0) {
            this.message = message;
            binding.setMessage(StringUtil.multilineString(message));
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}