package com.example.musicsearchapp;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class iTunes implements Serializable, Comparable<iTunes>
{
    public String trackName, genre, artist, album, albumPrice, date, urlToImage;
    public double trackPrice;
    //public Date date;

    public iTunes() {

    }

    public double getTrackPrice() {
        return trackPrice;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "iTunes{" +
                "trackName='" + trackName + '\'' +
                ", genre='" + genre + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", trackPrice='" + trackPrice + '\'' +
                ", albumPrice='" + albumPrice + '\'' +
                ", date='" + date + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }

    @Override
    public int compareTo(iTunes o) {
        return Comparators.DATE.compare(this,o);

    }

    public static class Comparators
    {
        public static Comparator<iTunes> DATE = new Comparator<iTunes>()
        {
            @Override
            public int compare(iTunes o1, iTunes o2)
            {
                try
                {
                    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date date1 = dateFormat.parse(o1.getDate());
                    Date date2 = dateFormat.parse(o2.getDate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };

        public static Comparator<iTunes> PRICE = new Comparator<iTunes>()
        {
            @Override
            public int compare(iTunes o1, iTunes o2) {
                if(o1.getTrackPrice()<o2.getTrackPrice())
                    return -1;
                if(o1.getTrackPrice() > o2.getTrackPrice())
                    return 1;
                return 0;
            }
        };
    }
}
