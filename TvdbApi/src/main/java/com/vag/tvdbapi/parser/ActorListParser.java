package com.vag.tvdbapi.parser;

import com.vag.tvdbapi.model.Actor;
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

public class ActorListParser implements XmlObjectListParser<Actor> {


    public Collection<Actor> parseListFromXmlString(String xml) throws XmlException {
        try {
            return readActorList(XmlUtil.getXmlPullParser(xml));
        } catch (IOException e) {
            throw new XmlException("Error reading XML String", e);
        } catch (XmlPullParserException e) {
            throw new XmlException("Error parsing XML", e);
        }
    }


    public Collection<Actor> parseListFromXmlStrings(Map<String, String> xmlStrings)
            throws XmlException {
        return parseListFromXmlString(xmlStrings.get("actors.xml"));
    }

    public Collection<Actor> readActorList(XmlPullParser parser)
            throws IOException, XmlPullParserException, XmlException {

        List<Actor> actorList = new ArrayList<Actor>();
        parser.require(XmlPullParser.START_TAG, null, "Actors");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            if (parser.getName().equals("Actor")) {
                Actor series = Actor.fromXml(parser);
                actorList.add(series);
            } else {
                XmlUtil.skip(parser);
            }
        }
        return actorList;
    }
}
