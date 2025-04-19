package com.example.lab44;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.widget.TextView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    // Конструктор адаптера
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаем View для каждого элемента списка
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Заполняем данные для текущей позиции
        Product product = productList.get(position);

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(String.format("Цена: $%.2f", product.getPrice()));
        holder.tvRating.setText(String.format("Рейтинг: %.1f (%d отзывов)",
                product.getRating().getRate(), product.getRating().getCount()));
        holder.tvCategory.setText(product.getCategory());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder для кэширования View-компонентов
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvRating, tvCategory;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
