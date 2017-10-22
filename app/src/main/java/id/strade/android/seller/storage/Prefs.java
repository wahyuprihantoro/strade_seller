package id.strade.android.seller.storage;

import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import id.strade.android.seller.model.User;


/**
 * Created by wahyu on 13 Maret 2017.
 */

@EBean(scope = EBean.Scope.Singleton)
public class Prefs {
    @Pref
    SharedPref_ sharedPref;

    Gson gson = new Gson();

    public String getToken() {
        return sharedPref.token().get();
    }

    public void setToken(String token) {
        sharedPref.edit().token().put(token).apply();
    }

    public User getUser() {
        return gson.fromJson(sharedPref.user().get(), User.class);
    }

    public void setUser(User user) {
        sharedPref.edit().user().put(gson.toJson(user)).apply();
    }

    public void logout() {
        sharedPref.edit().clear().apply();
    }
}
