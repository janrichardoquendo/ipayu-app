package ipayuv1.source.cfts.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ipayuv1.source.cfts.ipayu_v1.R;
import ipayuv1.source.cfts.listItems.Card_items;

import java.util.ArrayList;

/**
 * Created by michael on 5/22/2015.
 */
public class Card_adapter extends BaseAdapter{
    private Context context;
    private ArrayList<Card_items> CardListItem;

    public  Card_adapter(Context context, ArrayList<Card_items> CardListItem){
        this.context = context;
        this.CardListItem = CardListItem;
    }

    @Override
    public int getCount() {
        return CardListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return CardListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView cardName, cardNUmber;
        ImageView cardImage;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.card_item, null);
        }
        cardName = (TextView) convertView.findViewById(R.id.txtMerchantName);
        cardNUmber = (TextView) convertView.findViewById(R.id.txtCardNumber);
        cardImage = (ImageView) convertView.findViewById(R.id.imageView3);

        String cardNewName = CardListItem.get(position).getName();
        String cardNum = CardListItem.get(position).getNumber();
        String cardXMLFile = cardNewName.toLowerCase().replaceAll("\\W","_");
        Resources res = context.getResources();
        int resID = res.getIdentifier(cardXMLFile, "drawable", context.getPackageName());

        cardName.setText(cardNewName.toUpperCase());
        cardNUmber.setText(cardNum);
        cardImage.setBackgroundResource(resID);
        return convertView;
    }
}
