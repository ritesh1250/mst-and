package com.meest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.meest.Adapters.CollectionPointAdapter;
import com.meest.elasticsearch.ElasticSearchRespone;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElasticSearchActivity extends AppCompatActivity {

    private Context context;

    private RecyclerView recyclerSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elastic_search);
        context = ElasticSearchActivity.this;
        SearchView editTextTextPersonName3 = findViewById(R.id.editTextTextPersonName3);

        recyclerSearch = findViewById(R.id.recyclerSearch);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerSearch.setHasFixedSize(true);


        editTextTextPersonName3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    if (newText.length() >= 3)
                        getCollectionPoints(newText);
                    else
                        recyclerSearch.setAdapter(null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });


    }


    private void getCollectionPoints(String newText) {

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        String url ="http://34.229.13.180:9200/meestusers/_search?q="+newText+"*";
        Call<ElasticSearchRespone> call = webApi.elasticSearch(url);

        call.enqueue(new Callback<ElasticSearchRespone>() {
            @Override
            public void onResponse(Call<ElasticSearchRespone> call, Response<ElasticSearchRespone> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getHits() != null && response.body().getHits().getHits() != null && response.body().getHits().getHits().size() > 0) {
                            CollectionPointAdapter collectionPointAdapter = new CollectionPointAdapter(context, response.body().getHits().getHits());
                            recyclerSearch.setAdapter(collectionPointAdapter);
                        } else
                            Toast.makeText(ElasticSearchActivity.this, getString(R.string.no_data_available), Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<ElasticSearchRespone> call, Throwable t) {

            }
        });


    }


}