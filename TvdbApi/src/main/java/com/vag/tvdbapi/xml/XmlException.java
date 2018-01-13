package com.vag.tvdbapi.xml;

public class XmlException extends Exception {

    public XmlException(String message) {
        super(message);
    }

    public XmlException(String message, Throwable throwable) {
        super(message, throwable);
    }
}