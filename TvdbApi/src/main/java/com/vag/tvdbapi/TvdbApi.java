package com.vag.tvdbapi;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.vag.tvdbapi.model.Actor;
import com.vag.tvdbapi.model.Banner;
import com.vag.tvdbapi.model.Episode;
import com.vag.tvdbapi.model.Season;
import com.vag.tvdbapi.model.Series;
import com.vag.tvdbapi.parser.ActorListParser;
import com.vag.tvdbapi.parser.BannerListParser;
import com.vag.tvdbapi.parser.EpisodeParser;
import com.vag.tvdbapi.parser.SeasonListParser;
import com.vag.tvdbapi.parser.SeriesParser;
import com.vag.tvdbapi.xml.XmlObjectListRequest;
import com.vag.tvdbapi.xml.XmlObjectRequest;
import com.vag.tvdbapi.xml.ZippedXmlObjectListRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

@SuppressWarnings("unused")
public class TvdbApi {

    private static final String TAG = "TvdbApi";
    private static final String CHAR_ENCODING = "UTF-8";
    private static final String BASE_URL = "http://thetvdb.com/api/";
    private static final String SERIES_SEARCH = BASE_URL + "GetSeries.php?seriesname=";
    private static final String IMDB_SERIES_SEARCH = BASE_URL + "GetSeriesByRemoteID.php?imdbid=";

    private final String mApiKey;
    private final String mLanguage;
    private final RequestQueue mRequestQueue;

    /**
     * Create a new TvdbApi instance.
     *
     * @param apiKey       YTVDB api key
     * @param language     The two letter language code to use for queries, if null defaults to "en"
     * @param requestQueue The {@link RequestQueue} for api requests
     */
    public TvdbApi(String apiKey, String language, RequestQueue requestQueue) {
        mApiKey = apiKey;
        mLanguage = language;
        mRequestQueue = requestQueue;
    }
	
	/**
     * Get all of the {@link Season}s for a given {@link Series}
     *
     * @param series        {@link Series} to get the seasons for
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getSeasons(Series series, Response.Listener<Collection<Season>> listener,
                           Response.ErrorListener errorListener) {
        getSeasons(series.id, listener, errorListener);
    }

    /**
     * Get all of the {@link Season}s for a given TVDB Series ID
     *
     * @param seriesId      TVDB Series ID
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getSeasons(int seriesId, Response.Listener<Collection<Season>> listener,
                           Response.ErrorListener errorListener) {
        String requestUrl = getSeriesRequestUrl(seriesId);

        ZippedXmlObjectListRequest<Season, SeasonListParser> seasonRequest =
                new ZippedXmlObjectListRequest<Season, SeasonListParser>(
                        new SeasonListParser(mLanguage), requestUrl, listener, errorListener);

        mRequestQueue.add(seasonRequest);
    }
	
	/**
     * Get all of the {@link Episode}s for a given {@link Series}
     *
     * @param series        {@link Series} to get the episodes for
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisodes(Series series, Response.Listener<Collection<Episode>> listener,
                            Response.ErrorListener errorListener) {
        getEpisodes(series.id, listener, errorListener);
    }

    /**
     * Get all of the {@link Episode}s for a given TVDB Series ID
     *
     * @param seriesId      TVDB Series ID
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisodes(int seriesId, Response.Listener<Collection<Episode>> listener,
                            Response.ErrorListener errorListener) {
        getEpisodes(seriesId, EpisodeParser.ALL_SEASONS, listener, errorListener);
    }

    /**
     * Get the {@link Episode}s for a specific {@link Season}
     *
     * @param season        {@link Season} to get the episodes for
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisodes(Season season, Response.Listener<Collection<Episode>> listener,
                            Response.ErrorListener errorListener) {
        getEpisodes(season.seriesId, season.seasonNumber, listener, errorListener);
    }

    /**
     * Get {@link Episode}s for a given TVDB Series ID and season number
     *
     * @param seriesId      TVDB Series ID
     * @param seasonNumber  The season number
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisodes(int seriesId, int seasonNumber,
                            Response.Listener<Collection<Episode>> listener,
                            Response.ErrorListener errorListener) {
        String requestUrl = getSeriesRequestUrl(seriesId);

        ZippedXmlObjectListRequest<Episode, EpisodeParser> episodeRequest =
                new ZippedXmlObjectListRequest<Episode, EpisodeParser>(
                        new EpisodeParser(mLanguage, seasonNumber), requestUrl, listener,
                        errorListener);

        mRequestQueue.add(episodeRequest);
    }

    /**
     * Get a specific {@link Episode} with the default show order
     *
     * @param series        {@link Series} for the episode
     * @param seasonNumber  The season number
     * @param episodeNumber The episode number
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisode(Series series, int seasonNumber, int episodeNumber,
                           Response.Listener<Episode> listener,
                           Response.ErrorListener errorListener) {
        getEpisode(series, seasonNumber, episodeNumber, SHOW_ORDER.DEFAULT, listener,
                   errorListener);
    }

    /**
     * Get a specific {@link Episode} with a specific {@link SHOW_ORDER}
     *
     * @param series        {@link Series} for the episode
     * @param seasonNumber  The season number
     * @param episodeNumber The episode number
     * @param showOrder     The TVDB show order
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisode(Series series, int seasonNumber, int episodeNumber, SHOW_ORDER showOrder,
                           Response.Listener<Episode> listener,
                           Response.ErrorListener errorListener) {
        getEpisode(series.id, seasonNumber, episodeNumber, showOrder, listener, errorListener);
    }

    /**
     * Get a specific {@link Episode} with a specific {@link SHOW_ORDER}
     *
     * @param seriesId      TVDB Series ID
     * @param seasonNumber  The season number
     * @param episodeNumber The episode number
     * @param showOrder     The TVDB show order
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisode(int seriesId, int seasonNumber, int episodeNumber, SHOW_ORDER showOrder,
                           Response.Listener<Episode> listener,
                           Response.ErrorListener errorListener) {
        String showOrderModifier;
        if (showOrder == SHOW_ORDER.ABSOLUTE) {
            showOrderModifier = "/absolute/";
        } else if (showOrder == SHOW_ORDER.DVD) {
            showOrderModifier = "/dvd/";
        } else {
            showOrderModifier = "/default/";
        }
        String requestUrl =
                BASE_URL + mApiKey + "/series/" + seriesId + showOrderModifier + seasonNumber +
                "/" + episodeNumber + "/" + mLanguage + ".xml";

        XmlObjectRequest<Episode, EpisodeParser> episodeRequest = new XmlObjectRequest<Episode,
                EpisodeParser>(new EpisodeParser(mLanguage), requestUrl, listener, errorListener);

        mRequestQueue.add(episodeRequest);
    }

    /**
     * Get a specific {@link Episode} with a specific {@link SHOW_ORDER}
     *
     * @param season        The season
     * @param episodeNumber The episode number
     * @param showOrder     The TVDB show order
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getEpisode(Season season, int episodeNumber, SHOW_ORDER showOrder,
                           Response.Listener<Episode> listener,
                           Response.ErrorListener errorListener) {
        String showOrderModifier;
        if (showOrder == SHOW_ORDER.ABSOLUTE) {
            showOrderModifier = "/absolute/";
        } else if (showOrder == SHOW_ORDER.DVD) {
            showOrderModifier = "/dvd/";
        } else {
            showOrderModifier = "/default/";
        }
        String requestUrl =
                BASE_URL + mApiKey + "/series/" + season.seriesId + showOrderModifier + season.seasonNumber +
                "/" + episodeNumber + "/" + mLanguage + ".xml";

        XmlObjectRequest<Episode, EpisodeParser> episodeRequest = new XmlObjectRequest<Episode,
                EpisodeParser>(new EpisodeParser(mLanguage), requestUrl, listener, errorListener);

        mRequestQueue.add(episodeRequest);
    }
	
	/**
     * Search the TVDB for a {@link Series} based on the series name
     *
     * @param seriesName    The series to search for
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void searchSeries(String seriesName, Response.Listener<Collection<Series>> listener,
                             Response.ErrorListener errorListener) {
        String query;
        try {
            query = URLEncoder.encode(seriesName, CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "How the hell is " + CHAR_ENCODING + " not supported? Dropping request");
            return;
        }

        String requestUrl = SERIES_SEARCH + query + "&language=" + mLanguage;
        XmlObjectListRequest<Series, SeriesParser> seriesRequest =
                new XmlObjectListRequest<Series, SeriesParser>(new SeriesParser(),
                                                               requestUrl, listener,
                                                               errorListener);

        mRequestQueue.add(seriesRequest);
    }

    /**
     * Get a {@link Series} from the IMDB ID
     *
     * @param imdbId        The IMDB ID
     * @param listener      {@link Response.Listener} for receiving the result
     * @param errorListener {@link Response.ErrorListener} for receiving any errors
     */
    public void getSeriesFromImdbId(String imdbId, Response.Listener<Series> listener,
                                    Response.ErrorListener errorListener) {
        String requestUrl = IMDB_SERIES_SEARCH + imdbId;

        XmlObjectRequest<Series, SeriesParser> seriesRequest =
                new XmlObjectRequest<Series, SeriesParser>(new SeriesParser(), requestUrl, listener,
                                                           errorListener);

        mRequestQueue.add(seriesRequest);
    }

    private String getSeriesRequestUrl(int seriesId) {
        return BASE_URL + mApiKey + "/series/" + seriesId + "/all/" + mLanguage + ".zip";
    }

    public static enum SHOW_ORDER {DEFAULT, DVD, ABSOLUTE}
}
