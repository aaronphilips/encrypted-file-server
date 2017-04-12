import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

// http://viralpatel.net/blogs/java-md5-hashing-salting-password/
public final class ShadowFile{
  private ShadowFile(){}

  public static byte[] generateSalt(){
    final Random r = new SecureRandom();
    byte[] salt = new byte[32];
    r.nextBytes(salt);
    return salt;
  }
  public static void generateShadowFile(ArrayList<String> usernamePasswordList,byte[] salt){

    ArrayList<String> hashedUsernamePasswordList=new ArrayList<String>();
    for (String usernamePassword : usernamePasswordList) {
      System.out.println(usernamePassword);
      hashedUsernamePasswordList.add(hash_md5(usernamePassword,salt));
    }
    FileIO.deleteFile("shadowfile");
    FileIO.saveListToFile(hashedUsernamePasswordList,"shadowfile");


  }
  public static String hash_md5(String input,byte[] salt){
    String output=null;
    if(input==null)return output;
    try {
      MessageDigest messageDigest =  MessageDigest.getInstance("MD5");
      byte[] inputArray=input.getBytes();
      byte[] salted_input=new byte[inputArray.length+salt.length];
      System.arraycopy(inputArray, 0, salted_input, 0, inputArray.length);
      System.arraycopy(salt, 0, salted_input, inputArray.length, salt.length);
      messageDigest.update(salted_input,0,salted_input.length);
      output=new BigInteger(1, messageDigest.digest()).toString(16);
    }catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return output;

  }

}
