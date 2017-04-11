import java.net.Socket;
import java.util.Arrays;
// import java.io.BufferedReader;
// import java.io.DataOutputStream;
// import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Objects;
import javax.crypto.KeyAgreement;
// import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;

public class ServerRunnable implements Runnable,EncryptedCommunicator{
  private String clientSentence="";
  private String capitalizedSentence;
  private Socket socket;
  public ServerRunnable(Socket socket){
    this.socket=socket;
    try{
      KeyPair keyPair=generateKeys();
      privateKey=keyPair.getPrivate();
      publicKey=keyPair.getPublic();
      // System.out.println(privateKey.toString());
      // System.out.println(publicKey.toString());


    }catch(NoSuchAlgorithmException e){
      e.printStackTrace();
    }
  }

  PublicKey receivedPublicKey;
  PublicKey publicKey;
  private PrivateKey privateKey;
  private byte[] secretTEA_Key;


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
    // Socket connectionSocket =socket.accept();
    System.out.println("connected");


    try(ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream())){
      ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
      receivePublicKey((PublicKey) inFromClient.readObject());
      outToClient.writeObject(publicKey);
      generateCommonSecretKey();
      System.out.println("This is the TEA key"+new String(Arrays.toString(secretTEA_Key)));
      EncryptedMessageHandler encryptedMessageHandler=new EncryptedMessageHandler(secretTEA_Key);

      EncryptedMessage encryptedUserName= receiveEncrypted(inFromClient);//dssd(EncryptedMessage) inFromClient.readObject();
      System.out.println(encryptedMessageHandler.getString(encryptedUserName)+"|");

      EncryptedMessage encryptedPassword= receiveEncrypted(inFromClient);//(EncryptedMessage) inFromClient.readObject();
      System.out.println(encryptedMessageHandler.getString(encryptedPassword)+"|");



      // AUTHENTICATE
      // IF FAILED RETURN

      


    }catch(IOException e){
      e.printStackTrace();
    }catch(ClassNotFoundException e){
      e.printStackTrace();
    }
    System.out.println("Server runnable completed");
  }
}
