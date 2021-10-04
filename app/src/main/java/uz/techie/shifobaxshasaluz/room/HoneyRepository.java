package uz.techie.shifobaxshasaluz.room;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.techie.shifobaxshasaluz.models.Bonus;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.News;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.models.Product;
import uz.techie.shifobaxshasaluz.models.Seller;
import uz.techie.shifobaxshasaluz.models.User;
import uz.techie.shifobaxshasaluz.network.ApiClient;


public class HoneyRepository {
    private HoneyDao dao;
    Context context;
    private final String TAG = "repository";

    CompositeDisposable disposable;

    public HoneyRepository(Context context) {
        this.context = context;
        this.dao = HoneyDatabase.getInstance(context).dao();
        disposable = new CompositeDisposable();
    }


    //user
    public Completable insertUser(User user) {
        dao.deleteUser();
        return dao.insertUser(user);
    }

    public void deleteUser(){
        dao.deleteUser();
    }

    public User getUser() {
        return dao.getUser();
    }
    public LiveData<User> getUserLive() {
        return dao.getUserLive();
    }





    //products
    private void insertProducts(List<Product> products) {
        disposable.add(
                dao.insertProducts(products)
                        .toObservable()
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        );
    }

    private void deleteProducts(List<Product> products) {
        dao.deleteProducts()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        insertProducts(products);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public LiveData<List<Product>> getProducts() {
        return dao.getProducts();
    }

    public Product getSingleProduct(int productId){
        return dao.getSingleProduct(productId);
    }

    public void loadProducts() {
        ApiClient.getApiInterface().loadProducts()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Product> products) {
                        deleteProducts(products);
                        Log.d(TAG, "onNext: products " + products.size());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: products ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    //cart
    public Completable insertCart(Cart cart){
        return dao.insertCart(cart);
    }
    public Completable deleteCart(Cart cart){
        return dao.deleteCart(cart);
    }

    public Completable deleteAllCarts(){
        return dao.deleteAllCarts();
    }

    public List<Cart> getCarts(){
        return dao.getCarts();
    }

    public Integer getIncomingBonus(){
        return dao.getInComingBonus();
    }

    public LiveData<List<Cart>> getCartsLive(){
        return dao.getCartsLive();
    }

    public LiveData<Integer> getCartCount(){
        return dao.getCartsCount();
    }

    public Single<Integer> getTotalPriceInCart(){
        return dao.getTotalPriceInCart();
    }


    //seller

    public void insertSellers(List<Seller> sellers){
        disposable.add(
                dao.insertSellers(sellers)
                .subscribeOn(Schedulers.io())
                .subscribe()
        );
    }
    public void deleteSellers(List<Seller> sellers){
        dao.deleteSellers()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        insertSellers(sellers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
    public void loadSellers(String token){
        ApiClient.getApiInterface().loadSellers(token)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Seller>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Seller> sellers) {
                        deleteSellers(sellers);
                        Log.d(TAG, "onNext: sellers "+sellers.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: sellers ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public LiveData<List<Seller>> getSellersLive(){
        return dao.getSellersLive();
    }

    public LiveData<List<Seller>> getCustomersLive(){
        return dao.getCustomersLive();
    }



    //news
    public void insertNews(List<News> news){
        disposable.add(
                dao.insertNews(news)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        );
    }
    public void deleteNews(List<News> news){
        dao.deleteNews()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        insertNews(news);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
    public void loadNews(){
        ApiClient.getApiInterface().loadNews()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<News>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<News> news) {
                        deleteNews(news);
                        Log.d(TAG, "onNext: news "+news.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: news ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<List<News>> getNewsLive(){
        return dao.getNewsLive();
    }

    public LiveData<News> getSingleNews(int id){
        return dao.getSingleNews(id);
    }



    //history

    public void loadHistory(String token){
        ApiClient.getApiInterface().loadHistories(token)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<History>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<History> historyList) {
                        deleteHistory(historyList);

                        List<Order> orderList = new ArrayList<>();

                        for (History history:historyList){
                            orderList.addAll(history.getOperations_item());
                        }

                        deleteHistoryItems(orderList);


                        Log.d(TAG, "onNext: historyList "+historyList.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: historyList ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void insertHistory(List<History> histories){
        disposable.add(
                dao.insertHistory(histories)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        );
    }
    public void deleteHistory(List<History> histories){
        dao.deleteHistory()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        insertHistory(histories);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
    public LiveData<List<History>> getHistory(){
        return dao.getHistory();
    }

    public void deleteAllHistories(){
        dao.deleteHistory()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe();

        dao.deleteHistoryItems()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe();


    }



    //history items

    public void insertHistoryItems(List<Order> orders){
        disposable.add(
                dao.insertHistoryItem(orders)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        );
    }
    public void deleteHistoryItems(List<Order> orders){
        dao.deleteHistoryItems()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        insertHistoryItems(orders);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public LiveData<List<Order>> getHistoryItemsByItem(int historyId){
        return dao.getHistoryItemsById(historyId);
    }

    public List<Order> getHistoryItems(){
        return dao.getHistoryItems();
    }

    public LiveData<List<Order>> getHistoryItemsLive(){
        return dao.getHistoryItemsLive();
    }


    public void destroy() {
        disposable.clear();
    }


}





