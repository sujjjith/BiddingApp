package com.bid.utility;

import java.text.SimpleDateFormat;

/**
 * Created by skupunarapu on 1/8/2016.
 */
public class DatePattern {
    public final static String SDF_PATTERN = "yyyy-MM-dd'T'HH:mm";

    public static SimpleDateFormat getSdf() {
        return new SimpleDateFormat(SDF_PATTERN);
    }
}
