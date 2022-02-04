package com.meest.videomvvmmodule.view.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.meest.BuildConfig;
import com.meest.databinding.ActivityWalletBinding;
import com.meest.databinding.CustomToastBinding;
import com.meest.databinding.FragmentPurchaseCoinSheetBinding;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;

import com.meest.videomvvmmodule.viewmodel.CoinPurchaseViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;

public class CoinPurchaseSheetFragment extends BottomSheetDialogFragment implements PaymentResultListener {

    FragmentPurchaseCoinSheetBinding binding;
    CoinPurchaseViewModel viewModel;
    BillingProcessor bp;
    SessionManager sessionManager;
//    ActivityWalletBinding activityWalletBinding;
//    Context walletActivity;

//    public CoinPurchaseSheetFragment(WalletActivity walletActivity, ActivityWalletBinding binding) {
//        this.activityWalletBinding = binding;
//        this.walletActivity = walletActivity;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialog1;
            dialog.setCanceledOnTouchOutside(true);
        });
        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_coin_sheet, container, false);
        sessionManager = new SessionManager(getContext());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new CoinPurchaseViewModel()).createFor()).get(CoinPurchaseViewModel.class);
        initView();
        initListeners();
        initObserve();
        binding.setViewmodel(viewModel);
    }

    private void initView() {
        viewModel.fetchCoinPlans();
        Checkout.preload(getActivity().getApplicationContext());
//        if (getActivity() != null) {
//            bp = new BillingProcessor(getActivity(), getActivity().getResources().getString(R.string.play_lic_key), this);
//            bp.initialize();
//        }
    }

    private void initListeners() {
        viewModel.adapter.setOnRecyclerViewItemClick((data, position, binding) -> {
            binding.diamond.setVisibility(View.VISIBLE);
            binding.loutCoin.setBackgroundResource(R.drawable.ic_selection);
            viewModel.diamondQuantity = data.getCoinAmount();
            viewModel.diamondPrice = data.getCoinPlanPrice();
            viewModel.diamondId = data.getCoinPlanId();
//          bp.purchase(getActivity(), data.getPlaystoreProductId());
            viewModel.activeGateway(getActivity(), binding);

        });
    }

    private void initObserve() {
        viewModel.purchase.observe(getViewLifecycleOwner(), new Observer<RestResponse>() {
            @Override
            public void onChanged(RestResponse purchase) {
                CoinPurchaseSheetFragment.this.showPurchaseResultToast(purchase.getStatus());
            }
        });

    }

    private void showPurchaseResultToast(Boolean status) {
        dismiss();
        CustomToastBinding customToastBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.custom_toast, null, false);
        customToastBinding.setStatus(status);
        if (status != null && status) {
            customToastBinding.tvSendTitle.setText(getString(R.string.Sent_Successfull));
            customToastBinding.tvSendMessage.setText(getString(R.string.successful));
        } else {
            customToastBinding.tvSendTitle.setText(getString(R.string.error));
            customToastBinding.tvSendMessage.setText(getString(R.string.unsuccessful));

        }
        customToastBinding.loutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        customToastBinding.loutPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToastBinding.getRoot());
        toast.show();
    }


    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bp.handleActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            Toast.makeText(getActivity(), "nativeSdkForMerchantMessage" + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
            Log.e("=========status", "nativeSdkForMerchantMessage" + data.getStringExtra("response"));
        }
    }

    //    public void startPayment() {
//        final Checkout co = new Checkout();
//
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", "Meest");
//            options.put("description", "Payment");
//            //You can omit the image option to fetch the image from dashboard
////            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
//            options.put("currency", "INR");
//            options.put("prefill.contact", sessionManager.getUser().getData().getUserMobileNo());
//            options.put("prefill.email", sessionManager.getUser().getData().getUserEmail());
//            // amount is in paise so please multiple it by 100
//            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
//            // amount = amount * 100;
//            options.put("amount", Integer.parseInt(viewModel.coinsRate) * 100);
//
//              JSONObject preFill = new JSONObject();
//              preFill.put("email", "kamal.bunkar07@gmail.com");
//              preFill.put("contact", "9144040888");
//
//              options.put("prefill", preFill);
//
//            co.open(getActivity(), options);
//        } catch (Exception e) {
//            Log.e("====Error","Error in payment: " + e.getMessage());
//            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("harsh", "===============================payment successfull " + s.toString());
        Toast.makeText(getContext(), getString(R.string.Payment_successfully_done) + s, Toast.LENGTH_SHORT).show();
//        paymentStatus(s);
//        viewModel.purchaseCoin();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("harsh", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            Toast.makeText(getContext(), getString(R.string.Payment_error_please_try_again) + viewModel.diamondQuantity, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

}