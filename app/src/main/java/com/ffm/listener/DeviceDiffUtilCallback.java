package com.ffm.listener;

import android.os.Bundle;

import com.ffm.db.room.entity.Report;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import static com.ffm.constants.IntentConstants.REPORT_ID;


public class DeviceDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<Report> newList;
    ArrayList<Report> oldList;

    public DeviceDiffUtilCallback(ArrayList<Report> newList, ArrayList<Report> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Report newModel = newList.get(newItemPosition);
        Report oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();
        if (!newModel.getReportName().equals(oldModel.getReportName())
                || !newModel.getReportType().equals(oldModel.getReportType())) {
            diff.putSerializable(REPORT_ID, newModel);
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
        return newList.get(newItemPosition).getReportName().equals(oldList.get(oldItemPosition).getReportName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        return result == 0;
    }
}
