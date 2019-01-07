package com.ffm.binding;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;




public class ViewHolderBinding extends RecyclerView.ViewHolder{
    public final ViewDataBinding binding;

    public ViewHolderBinding(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}