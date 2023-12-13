package uz.techie.shifobaxshasaluz;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.models.Bonus;
import uz.techie.shifobaxshasaluz.network.ApiClient;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;
import uz.techie.shifobaxshasaluz.room.HoneyViewModelFactory;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    NavController navController;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    AppBarConfiguration appBarConfiguration;
    HoneyViewModel viewModel;
    String token = "";
    BonusDialog bonusDialog;

    MenuItem menuItem;

    TextView tvCounterOnCart;
    private int cartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new HoneyViewModelFactory(this)).get(HoneyViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connectFirebaseMessaging();

        if (Utils.shouldShowBonusDialog()){
            bonusDialog = new BonusDialog(this);
            bonusDialog.init();
            bonusDialog.showProgress();
            loadBonusInfo();
        }



        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.cabinetFragment)
                .build();

        bottomNavigationView = findViewById(R.id.main_bottomView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById( R.id.fragmentContainerView);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
        NavigationUI.setupWithNavController(toolbar, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                int id = destination.getId();
                if (id == R.id.phoneInputFragment || id == R.id.codeVerificationFragment  || id == R.id.cartSelectSellerFragment
                        || id == R.id.confirmOrderFragment || id == R.id.cartSelectCustomerFragment){
                    bottomNavigationView.setVisibility(View.GONE);
                }
                else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }

                if (id == R.id.newsFragment || id == R.id.cabinetFragment){
                    toolbar.setNavigationIcon(null);
                }

                if (id == R.id.cabinetFragment){
                    if (viewModel.getUser() == null){
                        navController.navigate(R.id.action_global_phoneInputFragment);
                    }
                }

            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();
        getCartCounts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menuItem = menu.findItem(R.id.cartFragment);
        View actionView = menuItem.getActionView();

        tvCounterOnCart = actionView.findViewById(R.id.action_cart_counter);
        tvCounterOnCart.setText(String.valueOf(cartCount));
        tvCounterOnCart.setVisibility(cartCount == 0? View.GONE:View.VISIBLE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cartFragment){

            if (!Utils.doesUserLogin() || viewModel.getUser() == null){
                navController.navigate(R.id.action_global_phoneInputFragment);
                return true;
            }
            navController.navigate(R.id.action_global_cartFragment);
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    private void requestPermissions(){
        PermissionX.init(this)
                .permissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.FOREGROUND_SERVICE)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, getString(R.string.reason_permissions), "Ok", "Cancel");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, getString(R.string.reason_permissions), "Ok", "Cancel");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {

                    }
                });
    }

    private void getCartCounts(){
        viewModel.getCartCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cartCount = integer;
                invalidateOptionsMenu();
            }
        });
    }

    public void loadBonusInfo(){
        ApiClient.getApiInterface().loadBonusInfo()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<List<Bonus>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Bonus> bonusList) {
                        bonusDialog.hideProgress();
                        bonusDialog.show(bonusList);
                        Log.d(TAG, "onNext: bonusList "+bonusList.size());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: historyList ", e);
                        bonusDialog.hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void connectFirebaseMessaging(){
        FirebaseMessaging.getInstance().subscribeToTopic("shifobaxsh_asal")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "not subscribed";
                        }
                        Log.d(TAG, msg);
                    }
                });
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        viewModel.loadProducts();
    }


}