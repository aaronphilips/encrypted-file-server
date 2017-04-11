// import java.nio.IntBuffer;
// import java.nio.ByteBuffer;

public class EncryptedMessageHandler{

  // since we are transporting this, its safe to give it the key
  private int[] key;
  public EncryptedMessageHandler(byte[] key){
    this.key=DataConverter.byteArrayToIntArray(key);
  }

  public int[] getDecryptedArray(EncryptedMessage encryptedMessage){
    int[] encryptedArray=encryptedMessage.getEncryptedArray();
    TEA tEA = new TEA();
    tEA.nativeDecrypt(encryptedArray,key);
    return encryptedArray;
  }

  public byte[] getByteArray(EncryptedMessage encryptedMessage){
    int[] intArray=getDecryptedArray(encryptedMessage);
    return DataConverter.intArrayToByteArray(intArray);
  }

  public String getString(EncryptedMessage encryptedMessage){
    return new String(getByteArray(encryptedMessage));
  }
}
