import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class DataConverter{

  private DataConverter(){}

  public static int[] byteArrayToIntArray(byte[] inputByteArray){

    byte[] byteArray=inputByteArray;

    if(byteArray.length<8){
      byteArray=new byte[byteArray.length+ 8 -(byteArray.length%8)];
      System.arraycopy(inputByteArray,0,byteArray,0,inputByteArray.length);
    }else if((byteArray.length%4)!=0){
      byteArray=new byte[byteArray.length+ 4 -(byteArray.length%4)];
      System.arraycopy(inputByteArray,0,byteArray,0,inputByteArray.length);
    }

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
    byte[] byteArray = trimTrailingNull(byteBuffer.array());

    return byteArray;
  }

  private static byte[] trimTrailingNull(byte[] bytes)
  {
    int i = bytes.length - 1;
    while (i >= 0 && bytes[i] == 0)
    {
        --i;
    }

    return Arrays.copyOf(bytes, i + 1);
  }
}
