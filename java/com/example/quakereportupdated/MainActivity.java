package com.example.quakereportupdated;

import androidx.appcompat.app.AppCompatActivity;

import androidx.loader.content.AsyncTaskLoader;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {
    private ListAdapter adapter;
    TextView emptyTextView;
    private static final String USGS_REQUEST_URL =
           "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&limit=20";

    private static final int EARTHQUAKE_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new ListAdapter(this,new ArrayList<Earthquake>());
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adapter);

        emptyTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyTextView); // IMPORTANT

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake = adapter.getItem(position);
                Intent earthquakeIntent = new Intent(Intent.ACTION_VIEW);
                earthquakeIntent.setData(Uri.parse(currentEarthquake.getUrl()));
                startActivity(earthquakeIntent);
            }
        });
//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);


        if (networkInfo!=null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else{
            emptyTextView.setText(R.string.no_connection);
        }
    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        View loadingIndictaor = findViewById(R.id.loading_indicator);
        loadingIndictaor.setVisibility(View.GONE);

        emptyTextView.setText(R.string.no_earthquakes);
        adapter.clear();
        if (earthquakes!= null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes); // adds ArrayList to the adapter
        }
    }

        @Override
        public void onLoaderReset (Loader < List < Earthquake >> loader) {
            adapter.clear();
        }

//    private class EarthquakeAsyncTask extends AsyncTask<String,Void, List<Earthquake>>{
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            List<Earthquake> earthquakes = new ArrayList<>();
//            if(urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//            earthquakes = QueryUtils.fetchEarthquakeData(urls[0]); //Important
//            return earthquakes;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> data) {
//            adapter.clear();
//            if (data != null && !data.isEmpty()) {
//                adapter.addAll(data); // adds ArrayList to the adapter
//            }
//        }
//    }
    }