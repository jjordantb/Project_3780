package com;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jordan on 11/26/2016.
 */
public class LogHandler {

    public static void log(final Object object) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) +"] " + object.toString());
    }

}
