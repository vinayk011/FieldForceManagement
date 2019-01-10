package com.ffm.util;

import com.ffm.FieldForceApplication;
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

    public static List<Report> readReportsJSONFile() {
        List<Report> reportsList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset()).getJSONArray("reports");
            int i = 0;
            while (jSONArray != null && i < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                reportsList.add(new Report(jSONObject.getString("reportType"), jSONObject.getString("reportName"),
                        jSONObject.getString("reportedBy"), jSONObject.getString("locationAddress"), jSONObject.getLong("reportedTime"),
                        jSONObject.getString("reportStatus"), jSONObject.getDouble("lat"), jSONObject.getDouble("lng")));
                i++;
            }
        } catch (Exception e) {
            Trace.e("Error parsing Reports.json");
        }
        Trace.i(" " + Arrays.toString(reportsList.toArray()));
        return reportsList;
    }

    public static String loadJSONFromAsset() {
        try {
            InputStream open = FieldForceApplication.getInstance().getAssets().open("Reports.json");
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
