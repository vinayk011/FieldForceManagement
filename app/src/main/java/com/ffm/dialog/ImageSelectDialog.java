package com.ffm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.ffm.R;
import com.ffm.databinding.DialogImageSelectBinding;
import com.ffm.listener.ImageSelectListener;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Raviteja on 19-03-2017. mybuddy_android
 */

public class ImageSelectDialog extends BaseDialog<DialogImageSelectBinding> {
    private Context context;
    private ImageSelectListener callback;
    private boolean imageAvailable;

    public ImageSelectDialog(Context context, boolean imageAvailable, ImageSelectListener callback) {
        super(context);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        this.context = context;
        this.callback = callback;
        this.imageAvailable = imageAvailable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_image_select, null, false);
        setContentView(binding.getRoot());
        binding.setCallback(imageSelectListener);
    }

    private ImageSelectListener imageSelectListener = new ImageSelectListener() {

        @Override
        public void onClickCamera() {
            callback.onClickCamera();
            dismiss();
        }

    };

    @Override
    public void dismiss() {
        super.dismiss();
        binding = null;
    }
}