import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Date;
import java.text.SimpleDateFormat;
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
import javax.crypto.KeyAgreement;
import java.util.Arrays;
import java.io.EOFException;

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
      // System.out.println("This is the TEA key clientside "+new String(Arrays.toString(secretTEA_Key)));
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
      if(!Objects.equals("ack",encryptedMessageHandler.getString(encryptedLoginAck))){
        System.out.println("login was not accepted");
        return;
      }
      System.out.println("STARTING FILE REQUESTS.");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
      String folderName=username+"_"+simpleDateFormat.format(new Date());
      while(true){
        System.out.println("NEW REQUEST. Type \".finished\" without the quotes to finish retrieving");
        System.out.print("Filename: ");
        String fileName = inFromUser.readLine();

        if(Objects.equals(fileName,".finished")) break;

        EncryptedMessage encryptedFileName=new EncryptedMessage(fileName,secretTEA_Key);
        sendEncrypted(encryptedFileName,outToServer);

        EncryptedMessage encryptedAck=receiveEncrypted(inFromServer);

        if(Objects.equals("ack",encryptedMessageHandler.getString(encryptedAck))){
          EncryptedMessage encryptedFile=receiveEncrypted(inFromServer);
          byte[] fileByteArray=encryptedMessageHandler.getByteArray(encryptedFile);
          FileIO.saveByteArrayToFile(fileByteArray,fileName,folderName);
        }else if(Objects.equals("fileNotFound",encryptedMessageHandler.getString(encryptedAck))){
          System.out.println("fileNotFound");
        }
      }
      clientSocket.close();
      System.out.println("Done. See directory "+folderName);
    }catch(UnknownHostException e){
      e.printStackTrace();
    }catch(EOFException e){
      System.out.println("Connection to server closed");
    }catch(IOException e){
      e.printStackTrace();
    }catch(ClassNotFoundException e){
      e.printStackTrace();
    }
    System.out.println("Client completed");
  }

  public static void main(String[] args) {

    Client client = new Client();
    client.run();

  }

}
