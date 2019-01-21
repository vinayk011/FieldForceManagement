package com.ffm.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.ffm.R;
import com.ffm.databinding.DialogCustomerResponseBinding;
import com.ffm.databinding.DialogImageSelectBinding;
import com.ffm.listener.ConfirmListener;
import com.ffm.listener.CustomerResponseListener;
import com.ffm.listener.DialogListener;
import com.ffm.listener.ImageSelectListener;
import com.ffm.util.Trace;

import androidx.databinding.DataBindingUtil;

public class CustomerResponseDialog extends BaseDialog<DialogCustomerResponseBinding> {
    private Context context;
    private CustomerResponseListener callback;

    public CustomerResponseDialog(Context context, CustomerResponseListener callback) {
        super(context);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_customer_response, null, false);
        setContentView(binding.getRoot());
        binding.setCallback(confirmListener);
    }

    ConfirmListener confirmListener = new ConfirmListener() {
        @Override
        public void ok() {
            callback.onAddResponse(binding.customerResponse.getText().toString());
            dismiss();
        }

        @Override
        public void cancel() {
            dismiss();
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        binding = null;
    }
}
