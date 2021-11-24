package uz.techie.shifobaxshasaluz.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.News;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.models.Product;
import uz.techie.shifobaxshasaluz.models.Seller;
import uz.techie.shifobaxshasaluz.models.User;


@Database(
        entities = {
                User.class,
                Product.class,
                Cart.class,
                Seller.class,
                News.class,
                Order.class,
                History.class
        },
        version = 19)
abstract class HoneyDatabase extends RoomDatabase {
    private static HoneyDatabase instance;

    public abstract HoneyDao dao();

    public static synchronized HoneyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), HoneyDatabase.class, "Honey_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
