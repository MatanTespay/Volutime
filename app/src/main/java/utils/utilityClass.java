package utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.caldroidsample.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static model.VolutimeDB.VOLUNTEER_COLUMN_VOLUPIC;

/**
 * helper class to preform custom help function
 * Created by Matan on 01/12/2016.
 */

public class utilityClass {

    private static utilityClass instance = null;
    private SimpleDateFormat longformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sortformat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sortformatter = new SimpleDateFormat("dd-MM-yyyy");
    static private Context context;

    public static utilityClass getInstance() {
        if (instance == null) {
            instance = new utilityClass();
        }
        return instance;
    }

    /**
     *
     * @return the long format of date
     */
    public  SimpleDateFormat getLongformatter() {
        return longformatter;
    }

    /**
     *
     * @return the short format of date
     */
    public  SimpleDateFormat getSortformatter() {
        return sortformatter;
    }

    /**
     * set the formats
     */
    public void setFormatter(){
        longformatter = new SimpleDateFormat(context.getResources().getString(R.string.LongDateStringFormat));
        sortformatter = new SimpleDateFormat(context.getResources().getString(R.string.DateStringFormat));
    }

    /**
     * convert String to long formatted date
     * @param dateString
     * @return
     */
    public Date getDateTimeFromString(String dateString){
        Date date = null;
        try {
                if(dateString != null && !dateString.equals(""))
                date = longformatter.parse(dateString);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  date;
    }

    /**
     * convert String to short formatted date
     * @param dateString
     * @return
     */
    public Date getSortDateTimeFromString(String dateString){
        Date date = null;
        try {
            if(dateString != null && !dateString.equals(""))
                date = sortformatter.parse(dateString);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  date;
    }

    /**
     * convert String to short formatted date (other format the the above method)
     * @param dateString
     * @return
     */
    public Date getDateFromString(String dateString){
        Date date = null;
        try {
            if(dateString != null && !dateString.equals(""))
                date = sortformat.parse(dateString);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  date;
    }

    /**
     * convert date to string of long formatted date
     * @param date
     * @return
     */
    public String getStringFromDateTime(Date date){
        String str= "";
        try {
            if(date != null)
            str = longformatter.format(date);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  str;
    }

    /**
     *  convert date to string of short formatted date
     * @param date
     * @return
     */
    public String getSortStringFromDateTime(Date date){
        String str= "";
        try {
            if(date != null)
            str = sortformatter.format(date);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  str;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * show a Toast
     * @param StringId string id to show
     * @param length Short or Long Toast
     * @param args params if needed to the string pattern
     */
    public void showToast(int StringId, int length,  Object... args){
        Resources res = this.context.getResources();
        String text;
        if(args != null)
            text = String.format(res.getString(StringId),args );
        else
            text = res.getString(StringId);

        Toast.makeText(this.context ,text , length).show();

    }

    /**
     * convert bitmap type to an array of byets
     * @param bitmap
     * @return
     */
    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * convert a bitmap object to string
     * @param imgByte
     * @return
     */
    public String imgToBase64String(Bitmap imgByte){
        String base64 = null;
        if (imgByte != null) {
            byte[] data = getBitmapAsByteArray(imgByte);
            if (data != null && data.length > 0) {
                base64 =  Base64.encodeToString(data, Base64.DEFAULT);

            }
        }

        return base64;
    }

    /**
     * gets a date , m,h,s and creates a long formatted Date with the input params.
     * @param date
     * @param H - hours
     * @param M - minutes
     * @param S - seconds
     * @return Date object
     */
    public Date dateTime(Date date, int H, int M, int S) {
        Calendar calendarA = Calendar.getInstance();
        calendarA.setTime(date);

        calendarA.set(Calendar.HOUR_OF_DAY, H);
        calendarA.set(Calendar.MINUTE, M);
        calendarA.set(Calendar.SECOND, S);
        Date result = calendarA.getTime();
        return  result;
    }

    /**
     *
     * @param startdate - the date that the event starts
     * @param enddate - the date that the event ends
     * @return a list of all the days between the given params to update the calendar
     */
    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().getTime() <= enddate.getTime())
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
}