package utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.caldroidsample.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Build.VERSION_CODES.M;

/**
 * helper class to preform custom help function
 * Created by Matan on 01/12/2016.
 */

public class utilityClass {

    private static utilityClass instance = null;
    private SimpleDateFormat longformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sortformatter = new SimpleDateFormat("dd MMM yyyy");
    static private Context context;

    public static utilityClass getInstance() {
        if (instance == null) {
            instance = new utilityClass();
        }
        return instance;
    }

    public  SimpleDateFormat getLongformatter() {
        return longformatter;
    }

    public  SimpleDateFormat getSortformatter() {
        return sortformatter;
    }

    public void setFormatter(){
        longformatter = new SimpleDateFormat(context.getResources().getString(R.string.LongDateStringFormat));
        sortformatter = new SimpleDateFormat(context.getResources().getString(R.string.DateStringFormat));
    }

    public Date getDateTimeFromString(String dateString){
        Date date = null;
        try {
                date = longformatter.parse(dateString);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  date;
    }

    public String getStringFromDateTime(Date date){
        String str= "";
        try {
            str = longformatter.format(date);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  str;
    }

    public String getSortStringFromDateTime(Date date){
        String str= "";
        try {
            str = sortformatter.format(date);
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return  str;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void showToast(int StringId,  Object... args){
        Resources res = this.context.getResources();
        String text;
        if(args != null)
            text = String.format(res.getString(StringId),args );
        else
            text = res.getString(StringId);

        Toast.makeText(this.context ,text , Toast.LENGTH_SHORT).show();

    }

    public Date dateTime(Date date, int H, int M, int S) {
        Calendar calendarA = Calendar.getInstance();
        calendarA.setTime(date);

        calendarA.set(Calendar.HOUR_OF_DAY, H);
        calendarA.set(Calendar.MINUTE, M);
        calendarA.set(Calendar.SECOND, S);
        Date result = calendarA.getTime();
        return  result;
    }

}