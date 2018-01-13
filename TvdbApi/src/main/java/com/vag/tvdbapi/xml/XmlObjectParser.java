package com.vag.tvdbapi.xml;

public interface XmlObjectParser<T> {
    public T parseXmlString(String xmlString) throws XmlException;
}