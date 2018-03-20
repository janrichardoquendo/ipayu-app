package ipayuv1.source.cfts.utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Executors;

/**
 * Created by oquendo on 5/22/15.
 */
public class SocketClient
{

    ChatClientThread chat = null;
    public void setChatClient(String name,String ip,int port)
    {
        chat = new ChatClientThread(name,ip,port);
        chat.start();
    }


    public void sendaMessage(String m)
    {
        if(chat==null)
        {
            return;
        }

        chat.sendMsg(m);
    }

    private String msg=null;

    public synchronized void newmsg(String x){
        msg=x;
    }

    public synchronized String getmsg(){
        String temp=msg;
        msg=null;
        return temp;
    }


    private class ChatClientThread extends Thread
    {
        String name;
        String dstAddress;
        String msg;
        int dstPort;

        String msgToSend = "";
        String msgLog = "";
        boolean goOut = false;

        ChatClientThread(String name, String address, int port) {
            this.name = name;
            dstAddress = address;
            dstPort = port;
        }

        public void run()
        {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            ObjectInputStream userlist = null;
            String msg="";

            try
            {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());

                dataInputStream = new DataInputStream(socket.getInputStream());
                userlist = new ObjectInputStream(socket.getInputStream());

                dataOutputStream.writeUTF(getIP());
                dataOutputStream.flush();


                while (!goOut)
                {
                    if (dataInputStream.available() > 0) {
                        msgLog += dataInputStream.readUTF();

                        Executors.newSingleThreadExecutor().execute(new Runnable() {

                            @Override
                            public void run() {

                                newmsg(msgLog);
                            }
                        });
                    }

                    if(!msgToSend.equals("")){
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }

                }

            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        private void sendMsg(String msg){
            msgToSend = msg;
        }

        private void disconnect(){
            goOut = true;
        }
    }

    private static String getIP()
    {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip +=inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

}
