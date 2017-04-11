// import java.nio.IntBuffer;
// import java.nio.ByteOrder;
// import java.nio.ByteBuffer;
import java.io.Serializable;
// import java.util.Arrays;
public class EncryptedMessage implements Serializable {
  private int[] encryptedArray;
  //see http://stackoverflow.com/questions/1168345/why-do-this-and-super-have-to-be-the-first-statement-in-a-constructor
  // pendor's answer on Jul 15 '11 at 13:47
  // private static int[] setupIntArray(byte[] byteArray){
  //   IntBuffer intBuf = ByteBuffer.wrap(byteArray)
  //                                .order(ByteOrder.BIG_ENDIAN)
  //                                .asIntBuffer();
  //   int[] value = new int[intBuf.remaining()];
  //   intBuf.get(value);
  //   return value;
  // }
  public EncryptedMessage(int[] value,byte[] key){
    // int [] copyArray=new int[value.length];
    // System.arraycopy(value,0,copyArray,0,value.length);
    int [] keyArray=DataConverter.byteArrayToIntArray(key);
    TEA tEA= new TEA();
    // System.out.print("this is value before encryption ");
    // System.out.println(Arrays.toString(value));
    // System.out.print("this is key before encryption ");
    // System.out.println(Arrays.toString(keyArray));
    tEA.nativeEncrypt(value,keyArray);
    // System.out.print("this is value after encryption ");
    // System.out.println(Arrays.toString(value));
    // System.out.print("this is key after encryption ");
    // System.out.println(Arrays.toString(keyArray));
    this.encryptedArray=value;
  }

  public EncryptedMessage(byte[] byteArray,byte[] key){
    this(DataConverter.byteArrayToIntArray(byteArray),key);
    // System.out.println("int arr "+Arrays.toString(DataConverter.byteArrayToIntArray(byteArray)));
  }

  public EncryptedMessage(String message,byte[] key){
    this(message.getBytes(),key);
    byte[] byteArray = message.getBytes();
    // System.out.println(Arrays.toString(byteArray));
    // System.out.println("byte array" +new String(byteArray));
    // System.out.println(byteArray.length);
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
