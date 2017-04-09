import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Objects;
public class Client{
  public static void main(String[] args) {
    String sentence;
    String modifiedSentence="";
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    try(Socket clientSocket = new Socket("localhost", 16000)){
      while(modifiedSentence==null||!Objects.equals(modifiedSentence,"DONE")){
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');
        modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);
      }

      clientSocket.close();
    }catch(UnknownHostException e){

    }catch(IOException e){}
  }
}
