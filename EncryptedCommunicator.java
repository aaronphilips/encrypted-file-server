import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public interface EncryptedCommunicator{

  default public KeyPair generateKeys() throws NoSuchAlgorithmException{
    final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
    keyPairGenerator.initialize(512);
    final KeyPair keyPair = keyPairGenerator.generateKeyPair();
    return keyPair;
  }

  public PublicKey getPublicKey();
  public void receivePublicKey(PublicKey receivedPublicKey);
  public void generateCommonSecretKey();
  default public void sendEncrypted(EncryptedMessage encryptedMessage,
                                    ObjectOutputStream objectOutputStream)
                                    throws IOException{
    objectOutputStream.writeObject(encryptedMessage);
    objectOutputStream.flush();
  }
  default public EncryptedMessage receiveEncrypted(ObjectInputStream objectInputStream)
                                            throws IOException, ClassNotFoundException{
    return (EncryptedMessage) objectInputStream.readObject();
  }

}
