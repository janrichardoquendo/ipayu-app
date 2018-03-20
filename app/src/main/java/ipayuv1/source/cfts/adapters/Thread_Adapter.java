package ipayuv1.source.cfts.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ipayuv1.source.cfts.ipayu_v1.R;
import ipayuv1.source.cfts.listItems.UserList_items;
import ipayuv1.source.cfts.utilities.ImageLoader;

/**
 * Created by michael on 5/22/2015.
 */
public class Thread_Adapter extends BaseAdapter{
    private Context context;
    private ArrayList<UserList_items> CardListItem;

    public Thread_Adapter(Context context, ArrayList<UserList_items> CardListItem){
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
        TextView userName;
        ImageView userImage;
        ImageLoader imageLoader = new ImageLoader(context);
        try {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.threaditem, null);
            }
            userImage = (ImageView) convertView.findViewById(R.id.userprofileimage);
            userName = (TextView) convertView.findViewById(R.id.contactName);

            String cardNewName = CardListItem.get(position).getName();
            String img = CardListItem.get(position).getImageUrl();
            if (img.equals("")) userImage.setImageResource(R.drawable.photo_not_available);
            else
                imageLoader.DisplayImage("http://centennialtech.biz/UploadToServer/profilepics/" + img, userImage);
            userName.setText(cardNewName.toUpperCase());

        } catch (Exception e) {
            Log.e("AdapterErr", e.toString());
        }
        return convertView;
    }
}
