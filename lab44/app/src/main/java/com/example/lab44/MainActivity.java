package com.example.lab44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProgressBar progressBar;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация UI элементов
        rvProducts = findViewById(R.id.rvProducts);
        progressBar = findViewById(R.id.progressBar);

        // Настройка RecyclerView
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(productList);
        rvProducts.setAdapter(adapter);

        // Загрузка данных
        new LoadProductsTask().execute();
    }
    // AsyncTask для загрузки данных в фоновом потоке
    private class LoadProductsTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            List<Product> products = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://fakestoreapi.com/products");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                // Парсинг JSON
                JSONArray jsonArray = new JSONArray(buffer.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productJson = jsonArray.getJSONObject(i);

                    // Парсинг рейтинга
                    JSONObject ratingJson = productJson.getJSONObject("rating");
                    Rating rating = new Rating(
                            ratingJson.getDouble("rate"),
                            ratingJson.getInt("count")
                    );

                    // Создание объекта Product
                    Product product = new Product(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getDouble("price"),
                            productJson.getString("description"),
                            productJson.getString("category"),
                            productJson.getString("image"),
                            rating
                    );

                    products.add(product);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);
            progressBar.setVisibility(View.GONE);

            if (products != null && !products.isEmpty()) {
                productList.clear();
                productList.addAll(products);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        }
    }
}