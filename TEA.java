import java.util.Arrays;

public class TEA {
  static{
    System.loadLibrary("_TEA");
  }
  public native void nativeEncrypt( int[] value, int[] key);
  public native void nativeDecrypt( int[] value, int[] key);



  public static void main(String[] args) {
    int[] key = new int[5];
    int[] value = new int[5];

    key[0]=1;
    key[1]=3;
    key[2]=5;
    key[3]=7;
    key[4]=9;
    value[0]=12;
    value[1]=34;
    value[2]=56;
    value[3]=78;
    value[4]=90;

    TEA tEA=new TEA();

    System.out.println(Arrays.toString(key));
    System.out.println(Arrays.toString(value));

    System.out.println("started encrypt");
    tEA.nativeEncrypt(value,key);
    System.out.println("done encrypt");

    System.out.println(Arrays.toString(key));
    System.out.println(Arrays.toString(value));

    System.out.println("started decrypt");
    tEA.nativeDecrypt(value,key);
    System.out.println("done decrypt");

    System.out.println(Arrays.toString(key));
    System.out.println(Arrays.toString(value));



  }

}
