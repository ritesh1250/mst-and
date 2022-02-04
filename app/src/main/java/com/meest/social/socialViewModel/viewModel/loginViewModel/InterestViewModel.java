package com.meest.social.socialViewModel.viewModel.loginViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityInterestModelBinding;
import com.meest.meestbhart.register.fragment.choosetopic.model.ChooseTopicResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicParam;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.UpdateSelectedTopicsResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.social.socialViewModel.adapter.ChooseAdapterSocial;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class InterestViewModel extends ViewModel {
    public ActivityInterestModelBinding binding;
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> isInterest = new MutableLiveData<>();
    public MutableLiveData<List<TopicsResponse.Row>> response_list = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public ChooseAdapterSocial adapter = new ChooseAdapterSocial();
    private CompositeDisposable disposable = new CompositeDisposable();


    public InterestViewModel(ActivityInterestModelBinding binding) {
        this.binding = binding;

    }

    public void chooseInterestApi() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(binding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));
//        map.put("x-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjdkODk4ZGJlLTYzN2YtNDdkOC1hMmQzLTFjZmY0NzQ2MzNkMSIsImVtY" +
//                "WlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMS0xMC0yN1QwNjo0ODozMS4xNjRaIn0sImlhdCI6MTYzNTMxNzMxMX0.IcdgstTR6nxIWtBaoW276XF4GmzK3pqhtx1IvXkP1vA");
        TopicParam topicParam = new TopicParam("50", "1");
        disposable.add(Global.initSocialRetrofit().getAll_topic(map, topicParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<TopicsResponse, Throwable>() {
                    @Override
                    public void accept(TopicsResponse topicsResponse, Throwable throwable) throws Exception {
                        if (topicsResponse.getCode() == 1) {
                            adapter.update(topicsResponse.getData().getRows());
                            int count = topicsResponse.getData().getRows().size();
                            binding.recyclerView.setItemViewCacheSize(count);

                        }
                    }
                }));
    }

    public void selectedInterest(ArrayList<String> array_list_) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(binding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));
        ChooseTopicResponse updateProfileParam = new ChooseTopicResponse(array_list_);
        disposable.add(Global.initSocialRetrofit().userTopics_insert(map, updateProfileParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<UpdateSelectedTopicsResponse, Throwable>() {
                    @Override
                    public void accept(UpdateSelectedTopicsResponse topicsResponse, Throwable throwable) throws Exception {
                        if (topicsResponse.getCode() == 1) {
                            isInterest.setValue(true);
                            SharedPrefreances.setSharedPreferenceString(binding.getRoot().getContext(), "ch_new", "0");
                            SocialPrefrences.setisInterest(binding.getRoot().getContext(), true);

                        }
                    }
                }));

    }
}
