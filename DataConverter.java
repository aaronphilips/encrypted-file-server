import java.nio.IntBuffer;
import java.nio.ByteBuffer;
// import java.nio.IntBuffer;
import java.nio.ByteOrder;
// import java.nio.ByteBuffer;
import java.util.Arrays;

public final class DataConverter{

  private DataConverter(){}

  public static int[] byteArrayToIntArray(byte[] inputByteArray){
    byte[] byteArray=inputByteArray;
    // System.out.print("the byte array's length: ");
    // System.out.println(byteArray.length);
    if(byteArray.length<8){
      // make size=byteArray.length+ 8 -(byteArray%8)
      byteArray=new byte[byteArray.length+ 8 -(byteArray.length%8)];
      System.arraycopy(inputByteArray,0,byteArray,0,inputByteArray.length);
    }else if((byteArray.length%4)!=0){
      // make size =byteArray.length+ 4 -(byteArray%4)
      byteArray=new byte[byteArray.length+ 4 -(byteArray.length%4)];
      System.arraycopy(inputByteArray,0,byteArray,0,inputByteArray.length);
    }
    // System.out.println("bytes "+Arrays.toString(byteArray));

    IntBuffer intBuf = ByteBuffer.wrap(byteArray)
                                 .order(ByteOrder.BIG_ENDIAN)
                                 .asIntBuffer();
    int[] value = new int[intBuf.remaining()];
    intBuf.get(value);
    return value;
  }

  public static byte[] intArrayToByteArray(int[] intArray){

    ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * 4);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(intArray);

    byte[] byteArray = byteBuffer.array();
    // System.out.println(new String(byteArray));
    return byteArray;
  }
  public static void main(String[] args) {
    byte[] byteArray="heedheede".getBytes();
    System.out.println("bytes "+Arrays.toString(byteArray));
    byteArrayToIntArray(byteArray);

  }

}
