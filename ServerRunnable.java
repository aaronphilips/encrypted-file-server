import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
  }

  PublicKey receivedPublicKey;
  PublicKey publicKey;
  private PrivateKey privateKey;
  private byte[] secretTEA_Key;


//

  public void receiveEncrypted(){}
  public void sendEncrypted(){}
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
    try{
      KeyPair keyPair=generateKeys();
      privateKey=keyPair.getPrivate();
      publicKey=keyPair.getPublic();
      System.out.println(privateKey.toString());
      System.out.println(publicKey.toString());


    }catch(NoSuchAlgorithmException e){
      e.printStackTrace();
    }

    try(BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

      // while(clientSentence==null||!Objects.equals(clientSentence,"done")){
      //
      //
      //   DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
      //   clientSentence = inFromClient.readLine();
      //   capitalizedSentence = clientSentence.toUpperCase() + '\n';
      //   outToClient.writeBytes(capitalizedSentence);
      // }
    }catch(IOException e){
      e.printStackTrace();
    }
    System.out.println("Server runnable completed");
  }
}
