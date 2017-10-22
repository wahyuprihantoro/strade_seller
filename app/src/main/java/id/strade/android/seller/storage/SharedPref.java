package id.strade.android.seller.storage;

import org.androidannotations.annotations.sharedpreferences.DefaultString;

/**
 * Created by wahyu on 13 Maret 2017.
 */

@org.androidannotations.annotations.sharedpreferences.SharedPref
public interface SharedPref {

    @DefaultString("")
    String token();

    //sharePref can't save double
    @DefaultString("0")
    String latitude();

    @DefaultString("0")
    String longitude();

    @DefaultString("")
    String user();

    @DefaultString("")
    String userLocation();

}
