package com.vag.tvdbapi.parser;

import android.util.Log;

import com.vag.tvdbapi.TvdbApi;
import com.vag.tvdbapi.model.Banner;
import com.vag.tvdbapi.model.Season;
import com.vag.tvdbapi.xml.XmlException;
import com.vag.tvdbapi.xml.XmlObjectListParser;
import com.vag.tvdbapi.xml.XmlUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SeasonListParser implements XmlObjectListParser<Season> {

    private final TvdbApi.SHOW_ORDER mShowOrder;
    private final String allEpisodes;

    public SeasonListParser(String language) {
        mShowOrder = TvdbApi.SHOW_ORDER.DEFAULT;
        allEpisodes = language + ".xml";
    }

    public Collection<Season> parseListFromXmlString(String xml) throws XmlException {
        try {
            return getBuiltSeasons(readSeasonList(XmlUtil.getXmlPullParser(xml)));
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        }
    }

    public Collection<Season> parseListFromXmlStrings(Map<String, String> xmlStrings)
            throws XmlException {
        try {
            Collection<Season.Builder> seasonBuilders =
                    readSeasonList(XmlUtil.getXmlPullParser(xmlStrings.get(allEpisodes)));
            Collection<Banner> banners =
                    readBannerList(XmlUtil.getXmlPullParser(xmlStrings.get("banners.xml")));
            return getBuiltSeasonsWithBanners(seasonBuilders, banners);
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        }
    }

    private Collection<Season.Builder> readSeasonList(XmlPullParser parser)
            throws IOException, XmlPullParserException, XmlException {
        Set<Season.Builder> seasons = new TreeSet<Season.Builder>(SEASON_NUMBER_ORDER);
        parser.require(XmlPullParser.START_TAG, null, "Data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("Episode")) {
                boolean added = seasons.add(Season.Builder.fromEpisodeXml(parser));
                if(added) {
                    Log.d("SeasonListParser", "Added season");
                }
            } else {
                XmlUtil.skip(parser);
            }
        }
        return seasons;
    }

    private Collection<Banner> readBannerList(XmlPullParser parser)
            throws IOException, XmlPullParserException, XmlException {
        Set<Banner> banners = new TreeSet<Banner>(BANNER_SEASON_NUMBER_ORDER);
        parser.require(XmlPullParser.START_TAG, null, "Banners");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;

            if (parser.getName().equals("Banner")) {
                Banner banner = Banner.fromXml(parser);
                if (banner.type.equals("season")) {
                    banners.add(banner);
                }
            } else {
                XmlUtil.skip(parser);
            }
        }
        return banners;
    }

    private Collection<Season> getBuiltSeasonsWithBanners(Collection<Season.Builder> sortedSeasons,
                                                          Collection<Banner> sortedBanners) {
        if (sortedSeasons.isEmpty() || sortedBanners.isEmpty()) {
            return getBuiltSeasons(sortedSeasons);
        }

        Iterator<Season.Builder> seasons = sortedSeasons.iterator();
        Season.Builder currentSeason = seasons.next();
        for (Banner banner : sortedBanners) {
            if (banner.seasonNumber == currentSeason.getSeasonNumber()) {
                currentSeason.addBanner(banner);
            } else {
                currentSeason = advanceToSeason(seasons, banner.seasonNumber);
                // If it returns null then we don't have any more banners for valid seasons
                if (currentSeason == null) break;
                currentSeason.addBanner(banner);
            }
        }

        return getBuiltSeasons(sortedSeasons);
    }

    private Season.Builder advanceToSeason(Iterator<Season.Builder> seasons, int seasonNumber) {
        while (seasons.hasNext()) {
            Season.Builder season = seasons.next();
            if (season.getSeasonNumber() == seasonNumber) return season;
        }
        return null;
    }

    private Collection<Season> getBuiltSeasons(Collection<Season.Builder> seasonBuilders) {
        Collection<Season> seasons = new ArrayList<Season>(seasonBuilders.size());
        for (Season.Builder builder : seasonBuilders) {
            seasons.add(builder.build());
        }
        return seasons;
    }

    private static final Comparator<Season.Builder> SEASON_NUMBER_ORDER =
            new Comparator<Season.Builder>() {

                public int compare(Season.Builder lhs, Season.Builder rhs) {
                    return lhs.getSeasonNumber() - rhs.getSeasonNumber();
                }
            };

    private static final Comparator<Banner> BANNER_SEASON_NUMBER_ORDER = new Comparator<Banner>() {

        public int compare(Banner lhs, Banner rhs) {
            return lhs.seasonNumber - rhs.seasonNumber;
        }
    };
}
