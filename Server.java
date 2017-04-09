import java.io.IOException;
import java.net.ServerSocket;

public class Server{
  public static void main(String[] args) {
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
