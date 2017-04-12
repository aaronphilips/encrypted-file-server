// import java.nio.IntBuffer;
// import java.nio.ByteOrder;
// import java.nio.ByteBuffer;
import java.io.Serializable;
// import java.util.Arrays;
public class EncryptedMessage implements Serializable {
  private int[] encryptedArray;
  public EncryptedMessage(int[] value,byte[] key){

    int [] keyArray=DataConverter.byteArrayToIntArray(key);
    TEA tEA= new TEA();
    tEA.nativeEncrypt(value,keyArray);
    this.encryptedArray=value;
  }

  public EncryptedMessage(byte[] byteArray,byte[] key){
    this(DataConverter.byteArrayToIntArray(byteArray),key);
    // System.out.println("int arr "+Arrays.toString(DataConverter.byteArrayToIntArray(byteArray)));
  }

  public EncryptedMessage(String message,byte[] key){
    this(message.getBytes(),key);
  }

  private EncryptedMessage(){}

  public int[] getEncryptedArray(){
    // return encryptedArray;
    // changed it because its weird to change the data the encrypted message has
    int [] returnArray=new int[encryptedArray.length];
    System.arraycopy(encryptedArray,0,returnArray,0,encryptedArray.length);
    return returnArray;
  }
}
