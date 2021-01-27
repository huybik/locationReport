package com.example.locationreport;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.google.android.gms.security.ProviderInstaller;

public class TemporaryProviderInstallerProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        try {
            ProviderInstaller.installIfNeeded(getContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /* The rest of the methods in this class are irrelevant and are only present to satisfy the abstract class requirements. */

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}