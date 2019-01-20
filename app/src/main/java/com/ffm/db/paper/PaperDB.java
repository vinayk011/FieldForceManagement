package com.ffm.db.paper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import io.paperdb.Book;
import io.paperdb.Paper;



public class PaperDB {
    private static PaperDB paperDB;

    private PaperDB() {
    }

    public static PaperDB getInstance() {
        if (paperDB == null) {
            paperDB = new PaperDB();
        }
        return paperDB;
    }

    public Book get() {
        return Paper.book();
    }

    public Book getInBand() {
        return Paper.book("BAND");
    }

    public boolean exists(String key) {
        return Paper.book().contains(key);
    }

    public void write(String key, Object value) {
        Paper.book().write(key, value);
    }

    public void destroy() {
        Paper.book().destroy();
    }


    public void remove(String key) {
        Paper.book().delete(key);
    }


    public void saveImageBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Paper.book().write(PaperConstants.PROFILE_PICTURE, encoded);
    }

    public Bitmap getImageBitmap() {
        String encoded = Paper.book().read(PaperConstants.PROFILE_PICTURE, null);
        if (encoded != null) {
            byte[] decodedBytes = Base64.decode(
                    encoded.substring(encoded.indexOf(",") + 1),
                    Base64.DEFAULT
            );
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return null;
    }
}