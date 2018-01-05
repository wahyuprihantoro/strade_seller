package id.strade.android.seller.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created by wahyu on 16 June 2017.
 */

public class Utils {

    public static final TextDrawable.IShapeBuilder I_SHAPE_BUILDER = TextDrawable.builder()
            .beginConfig()
            .width(36)
            .height(36)
            .textColor(Color.WHITE)
            .useFont(Typeface.DEFAULT)
            .toUpperCase()
            .endConfig();

    public static boolean isDigitOnly(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String rupiahFormat(String nominal) {
        Locale locale = new Locale("id", "ID");
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(locale);

        return rupiahFormat.format(Double.parseDouble(nominal));
    }

    public static String rupiahFormat(int nominal) {
        return rupiahFormat(nominal + "");
    }

    public static String convertTime(int time) {
        switch (time) {
            case 1:
                return "8.00 - 12.00 WIB";
            case 2:
                return "12.00 - 16.00 WIB";
            case 3:
                return "16.00 - 20.00 WIB";
            default:
                return "Fleksibel (kapan saja)";
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static TextDrawable getDefaultImage(@NonNull String name, @NonNull String username) {
        String[] nameArr = name.split(" ");
        String shortName = "";
        if (nameArr.length > 1) {
            shortName = nameArr[0].charAt(0) + "" + nameArr[1].charAt(0);
        } else {
            shortName = name.charAt(0) + "";
        }

        return I_SHAPE_BUILDER
                .buildRect(shortName, getUserColor(username));

    }

    public static int getUserColor(@NonNull String username) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        return generator.getColor(username);
    }
}
