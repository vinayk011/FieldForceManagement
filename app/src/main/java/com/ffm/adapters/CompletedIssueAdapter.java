package com.ffm.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.binding.ViewHolderBinding;
import com.ffm.databinding.ListItemTrackDetailsBinding;
import com.ffm.listener.ListItemListener;
import com.ffm.model.IssueHistory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import static com.ffm.constants.IntentConstants.ISSUE_ID;

public class CompletedIssueAdapter extends RecyclerView.Adapter {

    private ArrayList<IssueHistory> historyArrayList = new ArrayList<>();
    private Context mContext;

    public CompletedIssueAdapter(Context mContext, ArrayList<IssueHistory> histories) {
        this.mContext = mContext;
        this.historyArrayList = histories;
    }

    public void setHistoryList(ArrayList<IssueHistory> histories){
        this.historyArrayList = histories;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_track_details, parent, false);
        return new ViewHolderBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final IssueHistory history = historyArrayList.get(holder.getAdapterPosition());
        final ListItemTrackDetailsBinding itemDeviceBinding = (ListItemTrackDetailsBinding) ((ViewHolderBinding) holder).binding;
        itemDeviceBinding.setIssueDetails(history);
        itemDeviceBinding.executePendingBindings();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

}

