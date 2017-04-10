import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
public interface EncryptedCommunicator{
  // PUT IN COMMON INTERFACE
  default public KeyPair generateKeys() throws NoSuchAlgorithmException{
    // try {
         final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
         keyPairGenerator.initialize(512);

         final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //  privateKey = keyPair.getPrivate();
        //  publicKey  = keyPair.getPublic();
         return keyPair;
    //  } catch (Exception e) {
        //  e.printStackTrace();
    //  }
  }

  public PublicKey getPublicKey();


  public void receivePublicKey(PublicKey receivedPublicKey);
  public void generateCommonSecretKey();
  //
  // public void generateCommonSecretKey(){
  //   try {
  //         final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
  //         keyAgreement.init(privateKey);
  //         keyAgreement.doPhase(receivedPublicKey, true);
  //
  //         secretTEA_Key =  keyAgreement.generateSecret();
  //     } catch (Exception e) {
  //         e.printStackTrace();
  //     }
  // }
  public void sendEncrypted();
  public void receiveEncrypted();
  // public void setuphandshake();
}
