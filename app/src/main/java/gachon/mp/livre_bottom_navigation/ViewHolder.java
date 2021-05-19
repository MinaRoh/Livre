package gachon.mp.livre_bottom_navigation;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView vTitle, vContents, vNickname, vUploadTime;
    View mView;

    public ViewHolder(@NonNull View itemView){
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                mClickListener.onItemLongClick(v, getAdapterPosition());
                return false;
            }
        });


        //initialize views with model_layout.xml
        vTitle = itemView.findViewById(R.id.lTitle);
        vContents = itemView.findViewById(R.id.lContents);
        vNickname = itemView.findViewById(R.id.lNickname);
        vUploadTime = itemView.findViewById(R.id.lUploadTime);
    }
    private gachon.mp.livre_bottom_navigation.ViewHolder.ClickListener mClickListener;

    //interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    public void setOnClickListener(gachon.mp.livre_bottom_navigation.ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
