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
      // System.out.println(privateKey.toString());
      // System.out.println(publicKey.toString());


    }catch(NoSuchAlgorithmException e){
      e.printStackTrace();
    }
  }

  public void run(){
    String sentence;
    String modifiedSentence="";
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    try(Socket clientSocket = new Socket("localhost", 16000)){
      ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream inFromServer= new ObjectInputStream(clientSocket.getInputStream());


      // System.out.println(publicKey.getClass());
      // System.out.println(publicKey);

      sentence = inFromUser.readLine();
      outToServer.writeObject(publicKey);
      receivePublicKey((PublicKey) inFromServer.readObject());
      generateCommonSecretKey();
      System.out.println(new String(Arrays.toString(secretTEA_Key)));

      // outToServer.writeInt(publicKey);
      // while(modifiedSentence==null||!Objects.equals(modifiedSentence,"DONE")){
      //   DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      //   BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      //   sentence = inFromUser.readLine();
      //   outToServer.writeBytes(sentence + '\n');
      //   modifiedSentence = inFromServer.readLine();
      //   System.out.println(modifiedSentence);
      // }

      clientSocket.close();
    }catch(UnknownHostException e){

    }catch(IOException e){

    }catch(ClassNotFoundException e){
      e.printStackTrace();
    }
  }

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

  public static void main(String[] args) {

    Client client = new Client();
    client.run();

  }

}
