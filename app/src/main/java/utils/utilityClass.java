package utils;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * helper class to preform custom help function
 * Created by Matan on 01/12/2016.
 */

public class utilityClass {

    private static utilityClass instance = null;
    private Context context;

    public static utilityClass getInstance() {
        if (instance == null) {
            instance = new utilityClass();
        }
        return instance;
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
}