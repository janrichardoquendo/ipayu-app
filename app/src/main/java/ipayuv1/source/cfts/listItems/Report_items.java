package ipayuv1.source.cfts.listItems;

/**
 * Created by michael on 5/22/2015.
 */
public class Report_items {

    private String date, number, points;
    private int ID;

    public Report_items(){}

    public Report_items(String number, String points, String date, int ID){
        this.date = date;
        this.points = points;
        this.number = number;
        this.ID = ID;
    }

    public Report_items(String number, String points, String date){
        this.date = date;
        this.points = points;
        this.number = number;
    }

    public String getDate(){
        return this.date;
    }

    public String getPoints(){
        return this.points;
    }

    public String getNumber(){
        return this.number;
    }

    public int getID(){
        return this.ID;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setPoints(String points){
        this.points = points;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setID(int ID){
        this.ID = ID;
    }

}
