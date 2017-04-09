public class TEA {
  static{
    System.loadLibrary("_TEA");
  }
  public native int[] encrypt( int[] value, int[] key);

}
