import java.io.IOException;
import java.net.ServerSocket;

import java.util.Objects;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
// http://stackoverflow.com/questions/18268502/how-to-generate-salt-value-in-java
public class Server{
  protected byte[] salt;
  public Server(){
    FileIO.createDATA();
    System.out.println("Populate server with data in the DATA folder if you haven't already done so");
    String input = "";
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("please put the users and their passwords delimited by a comma. These will be saved in the shadow table");

    ArrayList<String> usernamePasswordList= new ArrayList<String>();

    try{
      while(!Objects.equals(input,".quit")){
        System.out.print("username,password:");
        input=inFromUser.readLine();
        usernamePasswordList.add(input);
      }
    }catch (IOException e) {
      e.printStackTrace();
      System.out.println("FAILED setting up users and passwords");
    }

    salt=ShadowFile.generateSalt();
    ShadowFile.generateShadowTable(usernamePasswordList,salt);
  }

  public static void main(String[] args) {
    Server server = new Server();
    System.out.println("SERVER RUNNINNG");
    try(ServerSocket serverSocket = new ServerSocket(16000)){
      while(true) {
        ServerRunnable serverRunnable= new ServerRunnable(serverSocket.accept(),server.salt);
        Thread serverThread=new Thread(serverRunnable);
        serverThread.start();
      }
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
