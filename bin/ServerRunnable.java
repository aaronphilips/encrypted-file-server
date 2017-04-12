import java.net.Socket;
import java.util.Arrays;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;
import javax.crypto.KeyAgreement;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class ServerRunnable implements Runnable,EncryptedCommunicator{
  private Socket socket;
  PublicKey receivedPublicKey;
  PublicKey publicKey;
  private PrivateKey privateKey;
  private byte[] secretTEA_Key;
  private byte[] salt;

  public ServerRunnable(Socket socket,byte[] salt){
    this.salt=salt;
    this.socket=socket;
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

    System.out.println("SERVER RUNNABLE STARTED");
    try(ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream())){
      ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
      receivePublicKey((PublicKey) inFromClient.readObject());
      outToClient.writeObject(publicKey);
      generateCommonSecretKey();

      EncryptedMessageHandler encryptedMessageHandler=new EncryptedMessageHandler(secretTEA_Key);

      EncryptedMessage encryptedUserName= receiveEncrypted(inFromClient);
      String username = encryptedMessageHandler.getString(encryptedUserName);

      EncryptedMessage encryptedPassword= receiveEncrypted(inFromClient);
      String password = encryptedMessageHandler.getString(encryptedPassword);

      String hash_salted =ShadowFile.hash_md5(username+","+password,salt);

      ArrayList<String> shadowfileList =FileIO.loadFileToList("shadowfile");

      if(!shadowfileList.contains(hash_salted)){
        System.out.println("CLIENT WITH INVALID CREDENTIALS");
        EncryptedMessage encryptedLoginAck =new EncryptedMessage("invalidLogin",secretTEA_Key);
        sendEncrypted(encryptedLoginAck,outToClient);
        return;
      }


      EncryptedMessage encryptedLoginAck =new EncryptedMessage("ack",secretTEA_Key);
      sendEncrypted(encryptedLoginAck,outToClient);
      while(true){
        System.out.println("NEW CLIENT REQUEST");

        EncryptedMessage encryptedFileName= receiveEncrypted(inFromClient);//(EncryptedMessage) inFromClient.readObject();
        String fileName=encryptedMessageHandler.getString(encryptedFileName);

        if(FileIO.checkFileExists(fileName,"DATA")){
          EncryptedMessage encryptedAck =new EncryptedMessage("ack",secretTEA_Key);
          sendEncrypted(encryptedAck,outToClient);
          byte[] fileByteArray = FileIO.loadFileToByteArray(fileName,"DATA");
          EncryptedMessage encryptedFile =new EncryptedMessage(fileByteArray,secretTEA_Key);
          sendEncrypted(encryptedFile,outToClient);
        }else{
          System.out.println("INVALID FILE REQUEST FROM: "+username);
          EncryptedMessage encryptedAck =new EncryptedMessage("fileNotFound",secretTEA_Key);
          sendEncrypted(encryptedAck,outToClient);
        }
      }
    }catch(ClassNotFoundException e){
      e.printStackTrace();
    }catch(EOFException e){
      System.out.println("CONNECTION TO CLIENT CLOSED");
    }catch(IOException e){
      e.printStackTrace();
    }
    System.out.println("SERVER RUNNABLE COMPLETED");
  }
}
