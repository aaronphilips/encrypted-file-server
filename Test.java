import java.util.Arrays;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
public class Test{
  public static void main(String[] args) {
    String string ="heed";
    byte[] byteArray = string.getBytes();
    System.out.println("bytes "+Arrays.toString(byteArray));
    System.out.println(new String(byteArray));
    System.out.println(byteArray.length);
    IntBuffer intBuf = ByteBuffer.wrap(byteArray)
                                 .order(ByteOrder.BIG_ENDIAN)
                                 .asIntBuffer();
    int[] array = new int[intBuf.remaining()];
    intBuf.get(array);
    System.out.println("int "+Arrays.toString(array));

    // ENCRYPT
    // DECRYPT
    // array should be the same ... right?

    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // DataOutputStream dos = new DataOutputStream(baos);
    // for(int i=0; i < array.length; ++i)
    // {
    //   try{
    //     dos.writeInt(array[i]);
    //   }catch(Exception e){}
    // }

    ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(array);

    byte[] barray = byteBuffer.array();

    System.out.println(new String(barray));


    //
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // DataOutputStream dos = new DataOutputStream(baos);
    // for(int i=0; i < values.length; ++i)
    // {
    //     dos.writeInt(values[i]);
    //   }
    //
    //   return baos.toByteArray();
    // }
  }
}
