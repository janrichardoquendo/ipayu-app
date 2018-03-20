package ipayuv1.source.cfts.listItems;

/**
 * Created by michael on 5/22/2015.
 */
public class Card_items {

    private String name, number, points, imageUrl;
    private int ID;

    public Card_items(){}

    public Card_items(String number, String points, String name, String imageUrl, int ID){
        this.name = name;
        this.points = points;
        this.number = number;
        this.imageUrl = imageUrl;
        this.ID = ID;
    }

    public Card_items(String number, String points, String name, String imageUrl){
        this.name = name;
        this.points = points;
        this.number = number;
        this.imageUrl = imageUrl;
    }

    public String getName(){
        return this.name;
    }

    public String getPoints(){
        return this.points;
    }

    public String getNumber(){
        return this.number;
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

    public void setPoints(String points){
        this.points = points;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setID(int ID){
        this.ID = ID;
    }

}
