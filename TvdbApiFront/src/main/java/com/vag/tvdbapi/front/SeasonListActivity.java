package com.vag.tvdb.front;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vag.tvdbapi.TvdbApi;
import com.vag.tvdbapi.TvdbItemAdapter;
import com.vag.tvdbapi.model.Banner;
import com.vag.tvdbapi.model.Season;
import com.vag.tvdbapi.model.Series;

import java.util.Collection;

public class SeasonListActivity extends Activity {

    public static final String EXTRA_SERIES = "series";

    private static final String TAG = "SeasonListActivity";

    private TvdbItemAdapter<Season> mSeasonAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App app = App.getInstance(this);
        mSeasonAdapter = new TvdbItemAdapter<Season>(this, app.getImageLoader(),
                                                     R.layout.tvdb_item, R.id.title,
                                                     R.id.image);

        setContentView(R.layout.grid_list);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(mOnSeasonSelectedListener);
        gridView.setAdapter(mSeasonAdapter);

        Intent intent = getIntent();
        Series series = intent.getParcelableExtra(EXTRA_SERIES);
        if (series != null) {
            TvdbApi tvdbApi = new TvdbApi(App.TVDB_API_KEY, "en", app.getRequestQueue());
            tvdbApi.getSeasons(series, mSeasonResponseListener, mErrorListener);
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private AdapterView.OnItemClickListener mOnSeasonSelectedListener =
            new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(SeasonListActivity.this,
                                   mSeasonAdapter.getItem(position).getTitleText(),
                                   Toast.LENGTH_SHORT).show();
                    Intent episodeList =
                            new Intent(SeasonListActivity.this, EpisodeListActivity.class);
                    episodeList.putExtra(EpisodeListActivity.EXTRA_SEASON,
                                         mSeasonAdapter.getItem(position));
                    startActivity(episodeList);
                }
            };

    private Response.Listener<Collection<Season>> mSeasonResponseListener =
            new Response.Listener<Collection<Season>>() {

                public void onResponse(Collection<Season> seasons) {
                    for (Season season : seasons) {
                        for (Banner banner : season.banners) {
                            Log.d(TAG, "type1: " + banner.type + " type2: " + banner.type2);
                        }
                    }
                    mSeasonAdapter.addAll(seasons);
                }
            };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(SeasonListActivity.this, "Oh noes! Something has gone awry.",
                           Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error fetching seasons: ", volleyError);
        }
    };
}
