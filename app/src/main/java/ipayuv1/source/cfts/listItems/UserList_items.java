package ipayuv1.source.cfts.listItems;

/**
 * Created by michael on 5/22/2015.
 */
public class UserList_items {

    private String name, imageUrl, p2;
    private int ID;

    public UserList_items(){}

    public UserList_items(String name, String imageUrl, String p2, int ID){
        this.name = name;
        this.imageUrl = imageUrl;
        this. p2 = p2;
        this.ID = ID;
    }

    public UserList_items(String name, String imageUrl, String p2){
        this.name = name;
        this. p2 = p2;
        this.imageUrl = imageUrl;
    }

    public String getName(){
        return this.name;
    }

    public String getP2(){
        return this.p2;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public int getID(){
        return this.ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setP2(String p2){
        this.p2 = p2;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setID(int ID){
        this.ID = ID;
    }

}
