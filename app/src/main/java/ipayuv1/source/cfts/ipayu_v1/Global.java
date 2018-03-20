package ipayuv1.source.cfts.ipayu_v1;

/**
 * Created by oquendo on 5/22/15.
 */
public class Global
{
    public static String ip ="centennialtech.biz";
    public static String y="";
    public static String userName="";
    public static String userID="";
    public static String phoneNumber = "";
    public static String myPhone="";
    public static String userFname="";
    public static final int SocketServerPORT = 8080;
    public static String IMAGE_PATH="";
    public static String IMAGE_URI="";
    public static boolean isDone=false;

    public static String newMemberAPI = "http://"+ip+"/TestAPI/index.php/registration/addUser";
    public static String checkUserAPI = "http://"+ip+"/TestAPI/index.php/registration/checkUser";
    public static String getCardAPI = "http://"+ip+"/TestAPI/index.php/registration/getCardInfo";
    public static String getThreadAPI = "http://"+ip+"/TestAPI/index.php/registration/getThread";
    public static String getUserInfo = "http://"+ip+"/TestAPI/index.php/registration/getUserInfo";
    public static String getConvo = "http://"+ip+"/TestAPI/index.php/registration/getConversation";
    public static String getConvo2 = "http://"+ip+"/TestAPI/index.php/registration/getConvoThread";
    public static String getAllUser = "http://"+ip+"/TestAPI/index.php/registration/getAllUsers";

    public static String imageloc = "http://"+ip+"/UploadToServer/chatimages/";
    public static String imagelocpath = "http://"+ip+"/UploadToServer/index.php";

    public static String addMsg = "http://"+ip+"/TestAPI/index.php/registration/addChatMessage";
    public static String addChat = "http://"+ip+"/TestAPI/index.php/registration/addChat";
    public static String addThread = "http://"+ip+"/TestAPI/index.php/registration/addThreadChat";

    public static String getTransactionsAPI = "http://"+ip+"/TestAPI/index.php/registration/getTransactions";
    public static String cardID="";
    public static String cardName="";
    public static String dateFrom="";
    public static String dateTo="";
}
