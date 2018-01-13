package com.vag.tvdbapi.parser;

import com.vag.tvdbapi.model.Episode;
import com.vag.tvdbapi.xml.XmlException;
import com.vag.tvdbapi.xml.XmlObjectListParser;
import com.vag.tvdbapi.xml.XmlObjectParser;
import com.vag.tvdbapi.xml.XmlUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EpisodeParser implements XmlObjectListParser<Episode>, XmlObjectParser<Episode> {
    public static final int ALL_SEASONS = -1;

    private final int mSeasonNumber;
    private final String allEpisodes;

    public EpisodeParser(String language) {
        allEpisodes = language + ".xml";
        mSeasonNumber = ALL_SEASONS;
    }

    public EpisodeParser(String language, int seasonNumber) {
        allEpisodes = language + ".xml";
        mSeasonNumber = seasonNumber;
    }


    public Collection<Episode> parseListFromXmlString(String xml) throws XmlException {
        try {
            return readEpisodeList(XmlUtil.getXmlPullParser(xml));
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        }
    }


    public Collection<Episode> parseListFromXmlStrings(Map<String, String> xmlStrings)
            throws XmlException {
        return parseListFromXmlString(xmlStrings.get(allEpisodes));
    }

    public Collection<Episode> readEpisodeList(XmlPullParser parser)
            throws IOException, XmlPullParserException, XmlException {

        List<Episode> episodeList = new ArrayList<Episode>();
        parser.require(XmlPullParser.START_TAG, null, "Data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("Episode")) {
                Episode episode = Episode.fromXml(parser);
                if (isValidEpisode(episode)) episodeList.add(episode);
            } else {
                XmlUtil.skip(parser);
            }
        }
        return episodeList;
    }

    private boolean isValidEpisode(Episode episode) {
        return episode != null && (episode.seasonNumber == mSeasonNumber || mSeasonNumber == -1);
    }


    public Episode parseXmlString(String xmlString) throws XmlException {
        try {
            XmlPullParser parser = XmlUtil.getXmlPullParser(xmlString);
            parser.require(XmlPullParser.START_TAG, null, "Episode");
            return Episode.fromXml(parser);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        }
    }
}
