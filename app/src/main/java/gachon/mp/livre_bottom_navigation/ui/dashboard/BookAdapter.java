package gachon.mp.livre_bottom_navigation.ui.dashboard;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    ArrayList<BookVO> array;
    Context context;

    public BookAdapter(ArrayList<BookVO> array, Context context) {
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image=array.get(position).getImage();
        holder.txttitle.setText(Html.fromHtml(array.get(position).getTitle()));
        holder.txtauthor.setText(Html.fromHtml(array.get(position).getAuthor()));

        Picasso.get().load(image).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txttitle;
        TextView txtauthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle=itemView.findViewById(R.id.txttitle);
            txtauthor=itemView.findViewById(R.id.txtauthor);
            image=itemView.findViewById(R.id.image);
        }
    }

}
