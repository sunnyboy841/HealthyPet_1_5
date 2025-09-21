package com.example.healthydoggy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SpotAdapter extends ArrayAdapter<Spot> {

    private Context context;
    private List<Spot> spots;

    public SpotAdapter(Context context, List<Spot> spots) {
        super(context, 0, spots);
        this.context = context;
        this.spots = spots;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.spot_item, parent, false);
        }

        Spot spot = spots.get(position);

        ImageView spotImage = convertView.findViewById(R.id.spotImage);
        TextView spotTitle = convertView.findViewById(R.id.spotTitle);
        TextView spotAddress = convertView.findViewById(R.id.spotAddress);
        TextView spotPrice = convertView.findViewById(R.id.spotPrice);
        TextView spotScore = convertView.findViewById(R.id.spotScore);

        // 设置数据
        spotTitle.setText(spot.getTitle());
        spotAddress.setText(spot.getAddress());
        spotPrice.setText("¥" + spot.getPrice() + "/人");
        spotScore.setText("评分: " + spot.getScore());

        // 加载图片（需要添加Picasso依赖）
        if (spot.getPic() != null && !spot.getPic().isEmpty()) {
            Picasso.get().load(spot.getPic().get(0))
                    .placeholder(R.drawable.ic_default_spot)
                    .error(R.drawable.ic_default_spot)
                    .into(spotImage);
        }

        return convertView;
    }
}