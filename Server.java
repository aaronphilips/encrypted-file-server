import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class Server{
  public static void main(String[] args) {
    String clientSentence;
    String capitalizedSentence;
    try(ServerSocket welcomeSocket = new ServerSocket(6789)){
      while(true) {
        Socket connectionSocket = welcomeSocket.accept();
        System.out.println("connected");
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        clientSentence = inFromClient.readLine();
        capitalizedSentence = clientSentence.toUpperCase() + '\n';
        outToClient.writeBytes(capitalizedSentence);

      }
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
