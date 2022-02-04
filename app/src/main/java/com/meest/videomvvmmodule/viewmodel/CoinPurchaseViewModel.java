package com.meest.videomvvmmodule.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cashfree.pg.CFPaymentService;
import com.meest.BuildConfig;
import com.meest.R;
import com.meest.databinding.ItemCoinPlansBinding;
import com.meest.videomvvmmodule.adapter.CoinPlansAdapter;
import com.meest.videomvvmmodule.model.CashFreeTransaction;
import com.meest.videomvvmmodule.model.DiamondPurchase;
import com.meest.videomvvmmodule.model.PaytmTransaction;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.wallet.MyWallet;
import com.meest.videomvvmmodule.utils.Global;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_BANK_CODE;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_CVV;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_HOLDER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_MM;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_NUMBER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_YYYY;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.cashfree.pg.CFPaymentService.PARAM_WALLET_CODE;

public class CoinPurchaseViewModel extends ViewModel {

    public CoinPlansAdapter adapter = new CoinPlansAdapter();
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public MutableLiveData<RestResponse> purchase = new MutableLiveData<>();
    public String diamondQuantity = "";
    public String diamondPrice = "";
    public String diamondId = "";
    private final CompositeDisposable disposable = new CompositeDisposable();
    public DiamondPurchase checksum_paytm;
    public CashFreeTransaction cashFreeTransaction;

    public String host = "";
    public MutableLiveData<MyWallet> myWallet = new MutableLiveData<>();

    public void fetchCoinPlans() {
        disposable.add(Global.initRetrofit().getCoinPlans(Global.apikey, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((purchase, throwable) -> {

                    if (purchase != null && purchase.getData() != null && !purchase.getData().isEmpty()) {
                        adapter.updateData(purchase.getData());
                    }
                }));
    }

    public void purchaseDiamond() {

        disposable.add(Global.initRetrofit().purchaseCoin(Global.apikey, diamondQuantity, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((purchase, throwable) ->
                {
                    if (purchase != null) {
                        this.purchase.setValue(purchase);
//                        fetchMyWallet();

                    }
                }));
    }

    public void test_paytm_checkSum(FragmentActivity activity, ItemCoinPlansBinding binding) {
        disposable.add(Global.initRetrofit().test_paytmCheckSum(Global.apikey, diamondPrice, Global.userId, diamondQuantity, diamondId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((purchase, throwable) -> {
                    if (purchase != null) {
                        checksum_paytm = purchase;
                        startPayment(activity, binding);
                    }
                }));
    }

    public void activeGateway(FragmentActivity activity, ItemCoinPlansBinding binding) {
        disposable.add(Global.initRetrofit().activeGateway(Global.apikey, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((activeGatewayResponse, throwable) -> {
                    if (activeGatewayResponse != null) {

                        if (BuildConfig.DEBUG) {
                            if (activeGatewayResponse.getGateway_id().equals("1")) {
                                test_paytm_checkSum(activity, binding);
                            } else {
                                test_cashfree_checkSum(activity, binding);
                            }

                        } else {
                            if (activeGatewayResponse.getGateway_id().equals("1")) {
                                paytm_checkSum(activity, binding);
                            } else {
                                cashfree_checkSum(activity, binding);
                            }
                        }
                    }
                }));
    }

    public void paytm_checkSum(FragmentActivity activity, ItemCoinPlansBinding binding) {
        disposable.add(Global.initRetrofit().paytmCheckSum(Global.apikey, diamondPrice, Global.userId, diamondQuantity, diamondId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((purchase, throwable) -> {
                    if (purchase != null) {
                        checksum_paytm = purchase;
                        startPayment(activity, binding);
                    }
                }));
    }

    public void test_cashfree_checkSum(FragmentActivity activity, ItemCoinPlansBinding binding) {
        disposable.add(Global.initRetrofit().test_cashFreeCheckSum(Global.apikey, diamondPrice, Global.userId, diamondQuantity, diamondId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((cashFreeTransaction, throwable) -> {
                    if (cashFreeTransaction != null) {
                        this.cashFreeTransaction = cashFreeTransaction;
                        startPaymentCashFree(activity, binding);
                    }
                }));
    }

    public void cashfree_checkSum(FragmentActivity activity, ItemCoinPlansBinding binding) {
        disposable.add(Global.initRetrofit().cashFreeCheckSum(Global.apikey, diamondPrice, Global.userId, diamondQuantity, diamondId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((cashFreeTransaction, throwable) -> {
                    if (purchase != null) {
                        this.cashFreeTransaction = cashFreeTransaction;
                        startPaymentCashFree(activity, binding);
                    }
                }));
    }

    public void startPayment(FragmentActivity activity, ItemCoinPlansBinding binding) {
        Log.e("TAG", "startPayment: " + checksum_paytm);

        if (BuildConfig.DEBUG) {
            host = "https://securegw-stage.paytm.in/";
        } else {
            host = "https://securegw.paytm.in/";
        }
//        host = "https://securegw.paytm.in/";

        String callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + checksum_paytm.getOrder_id();

        PaytmOrder paytmOrder = new PaytmOrder(checksum_paytm.getOrder_id(), checksum_paytm.getMid(), checksum_paytm.getData().getBody().getTxnToken(), diamondPrice, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle bundle) {
                Log.e("TAG", "Response (onTransactionResponse) : " + bundle.toString());
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
                add_transaction_details(checksum_paytm.getOrder_id(), bundle.getString("TXNID"), bundle.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS"), Global.gatewayPaytm);
            }

            @Override
            public void networkNotAvailable() {
                Log.e("TAG", "network not available ");
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }

            @Override
            public void onErrorProceed(String s) {
                Log.e("TAG", " onErrorProcess " + s.toString());
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e("TAG", "Clientauth " + s);
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.e("TAG", "UI error " + s);
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e("TAG", " error loading web " + s + "--" + s1);
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.e("TAG", "backPress ");
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e("TAG", " transaction cancel " + s);
                binding.diamond.setVisibility(View.INVISIBLE);
                binding.loutCoin.setBackgroundResource(R.drawable.bg_white_mayank);
            }
        });

        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(activity, 101);
    }

    public void startPaymentCashFree(FragmentActivity activity, ItemCoinPlansBinding binding) {

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        if (BuildConfig.DEBUG) {
            cfPaymentService.doPayment(activity, getInputParams(cashFreeTransaction), cashFreeTransaction.getData().getCftoken(), "TEST", "#143988", "#FFFFFF", false);
        } else {
            cfPaymentService.doPayment(activity, getInputParams(cashFreeTransaction), cashFreeTransaction.getData().getCftoken(), "PROD", "#143988", "#FFFFFF", false);
        }

//        cfPaymentService.doPayment(activity, getInputParams(cashFreeTransaction), cashFreeTransaction.getData().getCftoken(), "PROD", "#143988", "#FFFFFF", false);

    }

    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }

    SeamlessMode currentMode = SeamlessMode.CARD;

    private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams(cashFreeTransaction);
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, "VALID_CARD_NUMBER");
                params.put(PARAM_CARD_YYYY, "YYYY");
                params.put(PARAM_CARD_MM, "MM");
                params.put(PARAM_CARD_HOLDER, "CARD_HOLDER_NAME");
                params.put(PARAM_CARD_CVV, "CVV");
                break;
            case WALLET:
                params.put(PARAM_PAYMENT_OPTION, "wallet");
                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
                break;
            case NET_BANKING:
                params.put(PARAM_PAYMENT_OPTION, "nb");
                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
                break;
            case UPI_COLLECT:
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "VALID_VPA");
                break;
            case PAY_PAL:
                params.put(PARAM_PAYMENT_OPTION, "paypal");
                break;
        }
        return params;
    }

    private Map<String, String> getInputParams(CashFreeTransaction cashFreeTransaction) {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = cashFreeTransaction.getAppID();
        String orderId = cashFreeTransaction.getOrder_id();
        String orderAmount = diamondPrice;
        String orderNote = "Diamond Purchase";
        String customerName = cashFreeTransaction.getName();
        String customerPhone = cashFreeTransaction.getPhone();
        String customerEmail = "test@gmail.com";

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }


    public void add_transaction_details(String order_id, String testTransaction, boolean transactionStatus, String payment_gateway) {
        disposable.add(Global.initRetrofit().paytmTransaction(Global.apikey, Global.userId, order_id, testTransaction, payment_gateway)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((paytmTransaction, throwable) -> {
                    if (paytmTransaction != null) {
                        if (transactionStatus) {
                            purchaseDiamond();
                        }
                    }
                }));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    //    =========================================
//    public void fetchMyWallet() {
//        disposable.add(Global.initRetrofit().getMyWalletDetails(Global.apikey,Global.userId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe((wallet, throwable) -> {
//                    if (wallet != null && wallet.getStatus() != null) {
//                        myWallet.setValue(wallet);
//                    }
//                }));
}