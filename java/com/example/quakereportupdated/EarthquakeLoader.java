package com.example.quakereportupdated;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.app.LoaderManager;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String Url;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.Url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if (this.Url==null){
            return null;
        }
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(this.Url);
        return earthquakes;
    }
}
