package com.vag.tvdbapi.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.vag.tvdbapi.xml.XmlException;
import com.vag.tvdbapi.xml.XmlUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * TVDB Banner metadata
 */
public class Banner implements Parcelable {

    public final int id;
    public final String bannerPath;
    public final String thumbnailPath;
    public final String vignettePath;
    public final float rating;
    public final int ratingCount;
    public final boolean hasSeriesName;
    public final int lightAccentColor;
    public final int darkAccentColor;
    public final int neutralMidtoneColor;
    public final String type;
    public final String type2;
    public final String language;
    public final int seasonNumber;
    private static final String TAG = "Banner";
    private static final boolean D = false;
    private static final String TAG_ID = "id";
    private static final String TAG_BANNER_PATH = "BannerPath";
    private static final String TAG_THUMBNAIL_PATH = "ThumbnailPath";
    private static final String TAG_VIGNETTE_PATH = "VignettePath";
    private static final String TAG_BANNER_TYPE = "BannerType";
    private static final String TAG_BANNER_TYPE2 = "BannerType2";
    private static final String TAG_COLORS = "Colors";
    private static final String TAG_LANGUAGE = "Language";
    private static final String TAG_RATING = "Rating";
    private static final String TAG_RATING_COUNT = "RatingCount";
    private static final String TAG_HAS_SERIES_NAME = "SeriesName";
    private static final String TAG_SEASON = "Season";
    private static final String DELIMITER = "|";
    private static final int LIGHT_ACCENT_COLOR_POS = 0;
    private static final int DARK_ACCENT_COLOR_POS = 1;
    private static final int NEUTRAL_MIDTONE_COLOR_POS = 2;

    protected Banner(int id, String bannerPath, String thumbnailPath, String vignettePath,
                     float rating, int ratingCount, boolean hasSeriesName, int lightAccentColor,
                     int darkAccentColor, int neutralMidtoneColor, String type,
                     String type2, String language, int seasonNumber) {
        this.id = id;
        this.bannerPath = bannerPath;
        this.thumbnailPath = thumbnailPath;
        this.vignettePath = vignettePath;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.hasSeriesName = hasSeriesName;
        this.lightAccentColor = lightAccentColor;
        this.darkAccentColor = darkAccentColor;
        this.neutralMidtoneColor = neutralMidtoneColor;
        this.type = type;
        this.type2 = type2;
        this.language = language;
        this.seasonNumber = seasonNumber;
    }

    private Banner(Parcel in) {
        id = in.readInt();
        bannerPath = in.readString();
        thumbnailPath = in.readString();
        vignettePath = in.readString();
        rating = in.readFloat();
        ratingCount = in.readInt();
        hasSeriesName = in.readByte() != 0;
        lightAccentColor = in.readInt();
        darkAccentColor = in.readInt();
        neutralMidtoneColor = in.readInt();
        type = in.readString();
        type2 = in.readString();
        language = in.readString();
        seasonNumber = in.readInt();
    }

    public static Banner fromXml(XmlPullParser parser)
            throws XmlPullParserException, IOException, XmlException {
        Builder builder = new Builder();
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;

            String tag = parser.getName();
            if (tag.equals(TAG_ID)) {
                builder.setId(XmlUtil.readInt(parser, TAG_ID, TvdbItem.NOT_PRESENT));
            } else if (tag.equals(TAG_BANNER_PATH)) {
                builder.setBannerPath(XmlUtil.readText(parser, TAG_BANNER_PATH));
            } else if (tag.equals(TAG_THUMBNAIL_PATH)) {
                builder.setThumbnailPath(XmlUtil.readText(parser, TAG_THUMBNAIL_PATH));
            } else if (tag.equals(TAG_VIGNETTE_PATH)) {
                builder.setVignettePath(XmlUtil.readText(parser, TAG_VIGNETTE_PATH));
            } else if (tag.equals(TAG_BANNER_TYPE)) {
                builder.setType(XmlUtil.readText(parser, TAG_BANNER_TYPE));
            } else if (tag.equals(TAG_BANNER_TYPE2)) {
                builder.setType2(XmlUtil.readText(parser, TAG_BANNER_TYPE2));
            } else if (tag.equals(TAG_COLORS)) {
                builder.setColors(XmlUtil.readStringArray(parser, TAG_COLORS, DELIMITER));
            } else if (tag.equals(TAG_LANGUAGE)) {
                builder.setLanguage(XmlUtil.readText(parser, TAG_LANGUAGE));
            } else if (tag.equals(TAG_RATING)) {
                builder.setRating(XmlUtil.readFloat(parser, TAG_RATING, TvdbItem.NOT_PRESENT));
            } else if (tag.equals(TAG_RATING_COUNT)) {
                builder.setRatingCount(XmlUtil.readInt(parser, TAG_RATING_COUNT, TvdbItem.NOT_PRESENT));
            } else if (tag.equals(TAG_HAS_SERIES_NAME)) {
                builder.setHasSeriesName(XmlUtil.readBool(parser, TAG_HAS_SERIES_NAME));
            } else if (tag.equals(TAG_SEASON)) {
                builder.setSeasonNumber(XmlUtil.readInt(parser, TAG_SEASON, TvdbItem.NOT_PRESENT));
            } else {
                if (D) Log.w(TAG, "Skipping tag: " + tag);
                XmlUtil.skip(parser);
            }
        }
        return builder.build();
    }

    public static final Parcelable.Creator<Banner> CREATOR = new Parcelable.Creator<Banner>() {
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(bannerPath);
        out.writeString(thumbnailPath);
        out.writeString(vignettePath);
        out.writeFloat(rating);
        out.writeInt(ratingCount);
        out.writeByte((byte) ((hasSeriesName) ? 1 : 0));
        out.writeInt(lightAccentColor);
        out.writeInt(darkAccentColor);
        out.writeInt(neutralMidtoneColor);
        out.writeSerializable(type);
        out.writeSerializable(type2);
        out.writeString(language);
        out.writeInt(seasonNumber);
    }

    public static class Builder {
        private int id = TvdbItem.NOT_PRESENT;
        private String bannerPath = null;
        private String thumbnailPath = null;
        private String vignettePath = null;
        private float rating = TvdbItem.NOT_PRESENT;
        private int ratingCount = TvdbItem.NOT_PRESENT;
        private boolean hasSeriesName = false;
        private int lightAccentColor = TvdbItem.NOT_PRESENT;
        private int darkAccentColor = TvdbItem.NOT_PRESENT;
        private int neutralMidtoneColor = TvdbItem.NOT_PRESENT;
        private String type = null;
        private String type2 = null;
        private String language = null;
        private int season = TvdbItem.NOT_PRESENT;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setBannerPath(String bannerPath) {
            this.bannerPath = TvdbItem.BASE_IMAGE_URL + bannerPath;
            return this;
        }

        public Builder setThumbnailPath(String thumbnailPath) {
            this.thumbnailPath = TvdbItem.BASE_IMAGE_URL + thumbnailPath;
            return this;
        }

        public Builder setVignettePath(String vignettePath) {
            this.vignettePath = TvdbItem.BASE_IMAGE_URL + vignettePath;
            return this;
        }

        public Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }

        public Builder setRatingCount(int ratingCount) {
            this.ratingCount = ratingCount;
            return this;
        }

        public Builder setHasSeriesName(boolean hasSeriesName) {
            this.hasSeriesName = hasSeriesName;
            return this;
        }

        public Builder setColors(String[] colorStrings) {
            if (colorStrings.length == 3) {
                setLightAccentColor(getRgbIntFromCSV(colorStrings[LIGHT_ACCENT_COLOR_POS]));
                setDarkAccentColor(getRgbIntFromCSV(colorStrings[DARK_ACCENT_COLOR_POS]));
                setNeutralMidtoneColor(getRgbIntFromCSV(colorStrings[NEUTRAL_MIDTONE_COLOR_POS]));
            }
            return this;
        }

        public Builder setLightAccentColor(int lightAccentColor) {
            this.lightAccentColor = lightAccentColor;
            return this;
        }

        public Builder setDarkAccentColor(int darkAccentColor) {
            this.darkAccentColor = darkAccentColor;
            return this;
        }

        public Builder setNeutralMidtoneColor(int neutralMidtoneColor) {
            this.neutralMidtoneColor = neutralMidtoneColor;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setType2(String type2) {
            this.type2 = type2;
            return this;
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setSeasonNumber(int season) {
            this.season = season;
            return this;
        }

        public Banner build() {
            return new Banner(id, bannerPath, thumbnailPath, vignettePath, rating, ratingCount,
                              hasSeriesName, lightAccentColor, darkAccentColor, neutralMidtoneColor,
                              type, type2, language, season);
        }

        private int getRgbIntFromCSV(String csv) {
            String[] values = csv.split(",");
            if (values.length != 3) {
                return TvdbItem.NOT_PRESENT;
            } else {
                return Color.rgb(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                                 Integer.parseInt(values[2]));
            }
        }
    }
}