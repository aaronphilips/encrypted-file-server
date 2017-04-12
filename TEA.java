import java.util.Arrays;

public class TEA {
  static{
    System.loadLibrary("_TEA");
  }
  public native void nativeEncrypt( int[] value, int[] key);
  public native void nativeDecrypt( int[] value, int[] key);

}
