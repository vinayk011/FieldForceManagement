package com.ffm.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;


/**
 * Created by Raviteja on 13-03-2018 for HugFit.
 * Copyright (c) 2018 Hug Innovations. All rights reserved.
 */

public class BaseDialog<T extends ViewDataBinding> extends Dialog {
    Context context;
    T binding;

    BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }
}