package com.vag.tvdbapi.parser;

import com.vag.tvdbapi.model.Banner;
import com.vag.tvdbapi.xml.XmlException;
import com.vag.tvdbapi.xml.XmlObjectListParser;
import com.vag.tvdbapi.xml.XmlUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BannerListParser implements XmlObjectListParser<Banner> {

    public static final int ALL_SEASONS = -1;
    private int mSeasonNumber;

    @SuppressWarnings("unused")
    public BannerListParser() {
        mSeasonNumber = ALL_SEASONS;
    }

    public BannerListParser(int seasonNumber) {
        mSeasonNumber = seasonNumber;
    }


    public Collection<Banner> parseListFromXmlString(String xml) throws XmlException {
        try {
            return readBannerList(XmlUtil.getXmlPullParser(xml));
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        }
    }


    public Collection<Banner> parseListFromXmlStrings(Map<String, String> xmlStrings)
            throws XmlException {
        return parseListFromXmlString(xmlStrings.get("banners.xml"));
    }

    public Collection<Banner> readBannerList(XmlPullParser parser)
            throws IOException, XmlPullParserException, XmlException {

        List<Banner> bannerList = new ArrayList<Banner>();
        parser.require(XmlPullParser.START_TAG, null, "Banners");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("Banner")) {
                Banner banner = Banner.fromXml(parser);
                if (isValidBanner(banner)) bannerList.add(banner);
            } else {
                XmlUtil.skip(parser);
            }
        }
        return bannerList;
    }

    private boolean isValidBanner(Banner banner) {
        return banner != null &&
               (banner.seasonNumber == mSeasonNumber || mSeasonNumber == ALL_SEASONS);
    }
}
