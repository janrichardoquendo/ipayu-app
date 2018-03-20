package ipayuv1.source.cfts.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ipayuv1.source.cfts.ipayu_v1.Global;
import ipayuv1.source.cfts.ipayu_v1.R;
import ipayuv1.source.cfts.utilities.ImageLoader;
import ipayuv1.source.cfts.utilities.IpayuImageManager;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

	private TextView chatText;
	private ImageView img;
	private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
	private LinearLayout singleMessageContainer, message;
	final static String imageLocation=Global.imageloc;

	private Activity activity;
	public IpayuImageManager imageManager;


	public ChatArrayAdapter(Activity a, int textViewResourceId) {
		super(a, textViewResourceId);

		activity = a;
		imageManager = new IpayuImageManager(activity.getApplicationContext(),100000);

	}

	@Override
	public void add(ChatMessage object) {
		chatMessageList.add(object);
		super.add(object);
	}

	public void clearItem() {
		chatMessageList.clear();
	}

	public int getCount() {
		return this.chatMessageList.size();
	}

	public ChatMessage getItem(int index) {
		return this.chatMessageList.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder=null;
		ChatMessage chatMessageObj = getItem(position);
		ImageLoader imageLoader = new ImageLoader(activity);

		switch(chatMessageObj.action)
		{
			case 0:
				row = LayoutInflater.from(getContext()).inflate(R.layout.message, null);
				TextView msg = (TextView)row.findViewById(R.id.singleMessage);
				singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
				holder = new ViewHolder(msg);
				msg.setText(chatMessageObj.message);
				msg.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
				singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.RIGHT : Gravity.LEFT);
				break;

			case 1:
				row = LayoutInflater.from(getContext()).inflate(R.layout.imagemessage, null);
				ImageView img = (ImageView)row.findViewById(R.id.sentImg);
				holder = new ViewHolder(img);
				message = (LinearLayout) row.findViewById(R.id.message);
				singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);

				holder.img.setTag(imageLocation + "/" + chatMessageObj.path);

				imageLoader.DisplayImage(imageLocation + "/" + chatMessageObj.path, img);
//				imageManager.displayImage(imageLocation + "/" + chatMessageObj.path, activity, holder.img);
				singleMessageContainer.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
				message.setGravity(chatMessageObj.left ? Gravity.RIGHT : Gravity.LEFT);
				break;
			default:
				break;

		}

		row.setTag(holder);


		return row;
	}

	public class ViewHolder {
		TextView text;
		ImageView img;
		LinearLayout l;

		public ViewHolder(TextView text) {
			this.text = text;
		}

		public ViewHolder(ImageView im)
		{
			this.img = im;
		}

		public ViewHolder(LinearLayout l)
		{
			this.l = l;
		}

		public TextView getText() {
			return text;
		}

		public LinearLayout getL()
		{
			return l;
		}

		public void setText(TextView text) {
			this.text = text;
		}

		public ImageView getImg()
		{
			return img;
		}

		public void aView(ImageView img)
		{
			this.img = img;
		}

	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
}