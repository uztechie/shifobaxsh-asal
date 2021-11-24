package uz.techie.shifobaxshasaluz;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.multidex.MultiDexApplication;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uz.nisd.asalsavdosi.R;

public class Utils extends MultiDexApplication {

    private static String SHARED_PREF ="Shared_Prefs";
    private static String SHARED_PREF_USER ="User";
    private static String userState = "userState";
    private static String userLogin = "userLogin";
    private static String userType = "userType";
    private static String userPhone = "userPhone";
    private static String userId = "userId";
    private static String bonusDialogState = "bonusDialogState";


    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static final String IMAGE_URL = "https://qovunshirasi.uz/images/";



    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public static void setUserState(String state){
        editor.putString(userState, state);
        editor.commit();
    }

    public static String getUserState(){
        return sharedPreferences.getString(userState, "");
    }

    public static void setUserLogin(boolean doesUserLogin){
        editor.putBoolean(userLogin, doesUserLogin);
        editor.commit();
    }

    public static boolean doesUserLogin(){
        return sharedPreferences.getBoolean(userLogin, false);
    }

    public static boolean shouldShowBonusDialog(){
        return sharedPreferences.getBoolean(bonusDialogState, true);
    }
    public static void dontShowBonusDialog(){
        editor.putBoolean(bonusDialogState, false);
        editor.commit();
    }


    public static void setUserAsSeller(boolean isSeller){
        editor.putBoolean(userType, isSeller);
        editor.commit();
    }
    public static boolean isUserSeller(){
        return sharedPreferences.getBoolean(userType, false);
    }




    public static void setUserPhone(String phone){
        if (phone.contains("+")){
            phone = phone.replace("+","");
        }
        editor.putString(userPhone, phone);
        editor.commit();
    }

    public static String getUserPhone(){
        String phone = sharedPreferences.getString(userPhone, "");
        if (phone.contains("+"))
            phone = phone.replace("+","");
        return phone;
    }

    public static void setUserId(int id){
        editor.putInt(userId, id);
        editor.commit();
    }
    public static int getUserId(){
        return sharedPreferences.getInt(userId,0);
    }




    public boolean hasInternetConnection() {
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null) {
                networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }





    public static boolean hasInternetConnection(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null) {
                networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }



    public static void showProgressBar(KProgressHUD progressHUD) {

        progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void hideProgressBar(KProgressHUD progressHUD) {
        progressHUD.dismiss();
    }


    public static Bitmap getResizedBitmap(Bitmap bitmap, int maxSize){
        if (bitmap != null){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float bitmapRatio = (float) width/(float) height;
            if (bitmapRatio>1){
                width = maxSize;
                height = (int)(width/bitmapRatio);
            }
            else {
                height = maxSize;
                width = (int)(height*bitmapRatio);
            }
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        return null;
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }


    public static String moneyToDecimal(int money) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(money);
    }

    public static String formatDate(String text){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        if (date != null) {
            String formattedDate = dateFormat.format(date);
            return formattedDate;
        }

        return text;

    }

    public static String reFormatOnlyDate(String sDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (date != null) {
            String formattedDate = dateFormat.format(date);
            return formattedDate;
        }

        return sDate;
    }

    public static String reFormatOnlyDateInverse(String sDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateFormat = new SimpleDateFormat("MMM d, yyyy");
        if (date != null) {
            String formattedDate = dateFormat.format(date);
            return formattedDate;
        }

        return sDate;
    }



    public static void toastIconSuccess(Activity activity, String message) {
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = activity.getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_twotone_done_24);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(activity.getResources().getColor(R.color.green));

        toast.setView(custom_view);
        toast.show();
    }

    public static  void toastIconError(Activity activity, String message) {
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = activity.getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_close_24);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(activity.getResources().getColor(R.color.redd));

        toast.setView(custom_view);
        toast.show();
    }




    public static String reformatDate(String previousDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(previousDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (date != null){
            String formattedDate = dateFormat.format(date);
            return formattedDate;
        }

        return previousDate;

    }




}
