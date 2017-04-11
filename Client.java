import java.io.InputStreamReader;
import java.io.BufferedReader;
// import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Objects;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyAgreement;
import java.util.Arrays;
public class Client implements EncryptedCommunicator{

  PublicKey receivedPublicKey;
  PublicKey publicKey;
  private PrivateKey privateKey;
  private byte[] secretTEA_Key;

  public Client(){
    try{
      KeyPair keyPair=generateKeys();
      privateKey=keyPair.getPrivate();
      publicKey=keyPair.getPublic();
    }catch(NoSuchAlgorithmException e){
      e.printStackTrace();
    }
  }

  public void generateCommonSecretKey(){
    try {
      final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
      keyAgreement.init(privateKey);
      keyAgreement.doPhase(receivedPublicKey, true);
      secretTEA_Key =  keyAgreement.generateSecret();
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
  public PublicKey getPublicKey(){
    return publicKey;
  }

  public void receivePublicKey(PublicKey receivedPublicKey){
    this.receivedPublicKey=receivedPublicKey;
  }

  public void run(){

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    try(Socket clientSocket = new Socket("localhost", 16000)){
      ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream inFromServer= new ObjectInputStream(clientSocket.getInputStream());

      outToServer.writeObject(publicKey);
      outToServer.flush();
      receivePublicKey((PublicKey) inFromServer.readObject());
      generateCommonSecretKey();
      System.out.println("This is the TEA key clientside "+new String(Arrays.toString(secretTEA_Key)));
      EncryptedMessageHandler encryptedMessageHandler=new EncryptedMessageHandler(secretTEA_Key);


      // username
      System.out.print("USERNAME: ");
      String username = inFromUser.readLine();
      EncryptedMessage encryptedUsername=new EncryptedMessage(username,secretTEA_Key);
      sendEncrypted(encryptedUsername,outToServer);


      System.out.print("PASSWORD: ");
      String password = inFromUser.readLine();
      EncryptedMessage encryptedPassword=new EncryptedMessage(password,secretTEA_Key);
      sendEncrypted(encryptedPassword,outToServer);

      EncryptedMessage encryptedLoginAck=receiveEncrypted(inFromServer);
      String ackstring=new String(encryptedMessageHandler.getString(encryptedLoginAck)).trim();
      System.out.println(ackstring+" ack");
      System.out.println("ack".equals(ackstring));
      if(!"ack".equals(ackstring)){
        System.out.println("login was not accepted");
        return;
      }
      System.out.println("STARTING FILE REQUESTS");


      clientSocket.close();
    }catch(UnknownHostException e){

    }catch(IOException e){

    }catch(ClassNotFoundException e){
      e.printStackTrace();
    }
  }



  public static void main(String[] args) {

    Client client = new Client();
    client.run();

  }

}
