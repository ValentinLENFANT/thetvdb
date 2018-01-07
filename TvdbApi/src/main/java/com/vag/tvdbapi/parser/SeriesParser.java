package com.vag.tvdbapi.parser;

import com.vag.tvdbapi.model.Series;
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

public class SeriesParser implements XmlObjectListParser<Series>, XmlObjectParser<Series> {


    public Collection<Series> parseListFromXmlString(String xml) throws XmlException {
        try {
            return readSeriesList(XmlUtil.getXmlPullParser(xml));
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        }
    }


    public Collection<Series> parseListFromXmlStrings(Map<String, String> xmlStrings)
            throws XmlException {
        throw new IllegalStateException("Can't parse series list from a set of xmlStrings");
    }


    public Series parseXmlString(String xmlString) throws XmlException {
        try {
            return readSeries(XmlUtil.getXmlPullParser(xmlString));
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML");
        } catch (IOException e) {
            throw new XmlException("Error reading XML String");
        }
    }

    private Series readSeries(XmlPullParser parser)
            throws XmlPullParserException, IOException, XmlException {
        parser.require(XmlPullParser.START_TAG, null, "Data");
        parser.next();
        return Series.fromXml(parser);
    }

    private Collection<Series> readSeriesList(XmlPullParser parser)
            throws IOException, XmlPullParserException, XmlException {

        List<Series> SeriesList = new ArrayList<Series>();
        parser.require(XmlPullParser.START_TAG, null, "Data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("Series")) {
                Series series = Series.fromXml(parser);
                SeriesList.add(series);
            } else {
                XmlUtil.skip(parser);
            }
        }
        return SeriesList;
    }
}
