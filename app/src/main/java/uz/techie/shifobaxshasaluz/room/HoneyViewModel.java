package uz.techie.shifobaxshasaluz.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

public class HoneyViewModel extends AndroidViewModel {
    HoneyRepository repository;
    public HoneyViewModel(Application application) {
        super(application);
        repository = new HoneyRepository(application);
    }


    //user
    public Completable insertUser(User user){
        return repository.insertUser(user);
    }
    public User getUser(){
        return repository.getUser();
    }

    public void deleteUser(){
        repository.deleteUser();
    }

    public LiveData<User> getUserLive() {
        return repository.getUserLive();
    }



    //products
    public void loadProducts(){
        repository.loadProducts();
    }

    public LiveData<List<Product>> getProducts(){
        return repository.getProducts();
    }

    public Product getSingleProduct(int productId){
        return repository.getSingleProduct(productId);
    }

    //cart
    public Completable insertCart(Cart cart){
        return repository.insertCart(cart);
    }
    public Completable deleteCart(Cart cart){
        return repository.deleteCart(cart);
    }
    public Completable deleteAllCarts(){
        return repository.deleteAllCarts();
    }

    public List<Cart> getCarts(){
        return repository.getCarts();
    }

    public Integer getIncomingBonus(){
        return repository.getIncomingBonus();
    }

    public LiveData<List<Cart>> getCartsLive(){
        return repository.getCartsLive();
    }

    public LiveData<Integer> getCartCount(){
        return repository.getCartCount();
    }

    public Single<Integer> getTotalPriceInCart(){
        return repository.getTotalPriceInCart();
    }


    //sellers
    public LiveData<List<Seller>> getSellersLive(){
        return repository.getSellersLive();
    }
    public LiveData<List<Seller>> getCustomersLive(){
        return repository.getCustomersLive();
    }
    public void loadSellers(String token){
        repository.loadSellers(token);
    }


    //news

    public void loadNews(){
        repository.loadNews();
    }

    public LiveData<List<News>> getNewsLive(){
        return repository.getNewsLive();
    }

    public LiveData<News> getSingleNews(int id){
        return repository.getSingleNews(id);
    }


    //history

    public void deleteAllHistories(){
        repository.deleteAllHistories();
    }

    public void loadHistory(String token){
        repository.loadHistory(token);
    }

    public LiveData<List<History>> getHistory(){
        return repository.getHistory();
    }

    public LiveData<List<Order>> getHistoryItemsById(int historyId){
        return repository.getHistoryItemsByItem(historyId);
    }

    public List<Order> getHistoryItems(){
        return repository.getHistoryItems();
    }

    public LiveData<List<Order>> getHistoryItemsLive(){
        return repository.getHistoryItemsLive();
    }



}
