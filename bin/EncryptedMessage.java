import java.io.Serializable;

public class EncryptedMessage implements Serializable {
  /**
	 * Eclipse told me to do this when I made my uml
	 */
	private static final long serialVersionUID = 5727100107509104160L;

	private int[] encryptedArray;
  public EncryptedMessage(int[] value,byte[] key){

    int [] keyArray=DataConverter.byteArrayToIntArray(key);
    TEA tEA= new TEA();
    tEA.nativeEncrypt(value,keyArray);
    this.encryptedArray=value;
  }

  public EncryptedMessage(byte[] byteArray,byte[] key){
    this(DataConverter.byteArrayToIntArray(byteArray),key);
  }

  public EncryptedMessage(String message,byte[] key){
    this(message.getBytes(),key);
  }

  private EncryptedMessage(){}

  public int[] getEncryptedArray(){
    int [] returnArray=new int[encryptedArray.length];
    System.arraycopy(encryptedArray,0,returnArray,0,encryptedArray.length);
    return returnArray;
  }
}
