package com.ffm.util;

import android.content.Context;

import com.ffm.db.room.entity.Report;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    public static Gson getGson() {
        return gson;
    }

    public static List<Report> readReportsJSONFile(Context context) {
        List<Report> reportsList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset(context)).getJSONArray("reports");
            int i = 0;
            while (jSONArray != null && i < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                reportsList.add(new Report(jSONObject.getInt("complaintId"),jSONObject.getString("type"), jSONObject.getString("description"),
                        jSONObject.getString("reportedBy"), jSONObject.getString("locationAddress"), jSONObject.getLong("reportedTime"),
                        jSONObject.getString("complaintStatus"), jSONObject.getDouble("lat"), jSONObject.getDouble("lng")));
                i++;
            }
        } catch (Exception e) {
            Trace.e("Error parsing Reports.json");
        }
        Trace.i(" " + Arrays.toString(reportsList.toArray()));
        return reportsList;
    }

    public static String loadJSONFromAsset(Context context) {
        try {
            InputStream open = context.getAssets().open("complaints.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
