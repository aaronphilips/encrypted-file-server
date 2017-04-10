public class TEA {
  static{
    System.loadLibrary("_TEA");
  }
  public native void encrypt( int[] value, int[] key);
  public native void decrypt( int[] value, int[] key);

  public static void main(String[] args) {
    int[] key = new int[5];
    int[] value = new int[5];
    TEA tEA=new TEA();
    tEA.encrypt(value,key);
    tEA.decrypt(value,key);

  }

}
