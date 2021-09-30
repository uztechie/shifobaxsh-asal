package uz.techie.shifobaxshasaluz.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.News;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.models.Product;
import uz.techie.shifobaxshasaluz.models.Seller;
import uz.techie.shifobaxshasaluz.models.User;

@Dao
public interface HoneyDao {

    //user

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(User user);

    @Query("delete from user")
    void deleteUser();

    @Query("select * from user limit 1")
    User getUser();

    @Query("select * from user limit 1")
    LiveData<User> getUserLive();

    //products
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertProducts(List<Product> products);

    @Query("delete from products")
    Completable deleteProducts();

    @Query("select * from products order by published_date desc")
    LiveData<List<Product>> getProducts();

    @Query("select * from products where id=:productId")
    Product getSingleProduct(int productId);

    //cart
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCart(Cart cart);

    @Delete
    Completable deleteCart(Cart cart);

    @Query("select * from cart")
    List<Cart> getCarts();

    @Query("select sum(bonus*quantity) from cart")
    Integer getInComingBonus();

    @Query("select * from cart")
    LiveData<List<Cart>> getCartsLive();

    @Query("delete from cart")
    Completable deleteAllCarts();

    @Query("select count(id) from cart")
    LiveData<Integer> getCartsCount();

    @Query("select sum(price*quantity) from cart")
    Single<Integer> getTotalPriceInCart();


    //seller
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSellers(List<Seller> sellers);

    @Query("delete from seller")
    Completable deleteSellers();

    @Query("select * from seller where isSeller= 1 order by name")
    LiveData<List<Seller>> getSellersLive();

    @Query("select * from seller where isSeller= 0 order by name")
    LiveData<List<Seller>> getCustomersLive();


    //news

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNews(List<News> news);

    @Query("delete from news")
    Completable deleteNews();

    @Query("select * from news order by published_date desc")
    LiveData<List<News>> getNewsLive();

    @Query("select * from news where id=:id")
    LiveData<News> getSingleNews(int id);

    //history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertHistory(List<History> histories);

    @Query("delete from order_history")
    Completable deleteHistory();

    @Query("select * from order_history order by created_at desc")
    LiveData<List<History>> getHistory();


    //history item

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertHistoryItem(List<Order> orders);

    @Query("delete from order_item_history")
    Completable deleteHistoryItems();

    @Query("select * from order_item_history where `order`=:historyId")
    LiveData<List<Order>> getHistoryItemsById(int historyId);

    @Query("select * from order_item_history")
    List<Order> getHistoryItems();

    @Query("select * from order_item_history")
    LiveData<List<Order>> getHistoryItemsLive();






}

