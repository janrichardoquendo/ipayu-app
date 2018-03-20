package ipayuv1.source.cfts.adapters;

import android.widget.ImageView;

public class ChatMessage {
	public boolean left;
	public String message,path;
	public int action;


	public ChatMessage(boolean left, String message,int action,String path)
	{
		super();
		this.left = left;
		this.message = message;
		this.action = action;
		this.path = path;

	}
}