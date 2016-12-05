package com.vansh.resellerprofit.utility;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityMethods {
    public static boolean isNetworkAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public static String getTimeString(int year, int month, int day, int hour, int minute){
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd hh mm");
        try {
            Date date = dateFormat.parse(year + " " + month + " " + day + " " + hour + " " + minute);
            long time = date.getTime();
            return Long.toString(time/60000);
        }catch (Exception e){
            return null;
        }
    }

    public static String getFormattedTimeString(String timestamp){
        return new SimpleDateFormat("MM/dd/yyyy HH:mm").format(timestamp);
    }
}
