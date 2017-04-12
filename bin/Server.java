import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;

public class Server{
  protected byte[] salt;

  private void createDATA(){
    try{
      Files.createDirectories(Paths.get("./DATA"));
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Server(){
    createDATA();
    System.out.println("Populate server with data in the DATA folder if you haven't already done so");
    String input = "";
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("please put the users and their passwords delimited by a comma. These will be saved in the shadow table. Stop entering users by entering \".done\" without quotes");

    ArrayList<String> usernamePasswordList= new ArrayList<String>();

    try{
      while(!Objects.equals(input,".done")){
        System.out.print("username,password:");
        input=inFromUser.readLine();
        usernamePasswordList.add(input);
      }
      usernamePasswordList.remove(usernamePasswordList.size()-1);
    }catch (IOException e) {
      e.printStackTrace();
      System.out.println("FAILED SETTING UP USERS AND PASSWORDS");
    }

    salt=ShadowFile.generateSalt();
    ShadowFile.generateShadowFile(usernamePasswordList,salt);
  }

  public static void main(String[] args) {
    Server server = new Server();
    System.out.println("SERVER RUNNING");
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
