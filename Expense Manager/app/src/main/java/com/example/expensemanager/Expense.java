package com.example.expensemanager;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Expense implements Serializable, Comparable<Expense> {
    String name, date, key, image_url;
    double cost;

    public Expense() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", key='" + key + '\'' +
                ", cost=" + cost + '\'' +
                ", image_url=" + image_url +
                '}';
    }

    @Override
    public int compareTo(Expense o) {
        return Comparators.DATE.compare(this,o);

    }

    public static class Comparators
    {
        public static Comparator<Expense> DATE = new Comparator<Expense>()
        {
            @Override
            public int compare(Expense o1, Expense o2)
            {
                try
                {
                    SimpleDateFormat sdf=new SimpleDateFormat("MMM, dd yyyy");
                    Date date=sdf.parse(o1.date);
                    sdf=new SimpleDateFormat("dd/MM/yyyy");
                    String str_date1 = sdf.format(date);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date d1 = dateFormat.parse(str_date1);

                    Date date2=sdf.parse(o2.date);
                    sdf=new SimpleDateFormat("dd/MM/yyyy");
                    String str_date2 = sdf.format(date2);
                    Date d2 = dateFormat.parse(str_date2);
//                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate());

                    //Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate());
                    Log.d("demo","dates"+ d1+" "+d2);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        };

        public static Comparator<Expense> COST = new Comparator<Expense>()
        {
            @Override
            public int compare(Expense o1, Expense o2) {
                if(o1.getCost()<o2.getCost())
                    return -1;
                if(o1.getCost() > o2.getCost())
                    return 1;
                return 0;
            }
        };
    }
}
