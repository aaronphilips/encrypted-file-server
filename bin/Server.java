import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    try {
      ShadowFile.generateShadowFile();
    }catch (Exception e) {
      e.printStackTrace();
      System.out.println("FAILED SETTING UP USERS AND PASSWORDS");
    }    
  }

  public static void main(String[] args) {
    Server server = new Server();
    System.out.println("SERVER RUNNING");
    try(ServerSocket serverSocket = new ServerSocket(16000)){
      while(true) {
        ServerRunnable serverRunnable= new ServerRunnable(serverSocket.accept());
        Thread serverThread=new Thread(serverRunnable);
        serverThread.start();
      }
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
