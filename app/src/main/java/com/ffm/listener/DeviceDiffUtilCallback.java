package com.ffm.listener;

import android.os.Bundle;

import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.entity.Report;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import static com.ffm.constants.IntentConstants.ISSUE_ID;



public class DeviceDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<Complaint> newList;
    ArrayList<Complaint> oldList;

    public DeviceDiffUtilCallback(ArrayList<Complaint> newList, ArrayList<Complaint> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Complaint newModel = newList.get(newItemPosition);
        Complaint oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();
        if (!newModel.getDescription().equals(oldModel.getDescription())
                || !newModel.getIssueStatus().equals(oldModel.getIssueStatus())) {
            diff.putSerializable(ISSUE_ID, newModel);
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getDescription().equals(oldList.get(oldItemPosition).getDescription());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        return result == 0;
    }
}
