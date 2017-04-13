import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Objects;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.IOException;


public final class ShadowFile{
  private ShadowFile(){}

  private static String generateSalt(){
    final Random random = new SecureRandom();
    byte[] saltBytes = new byte[16];
    random.nextBytes(saltBytes);
    String salt=Base64.getEncoder().encodeToString(saltBytes);
    return salt;
  }

  public static void generateShadowFile() throws IOException{
    String input = "";
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    Boolean shadowfileExists=FileIO.checkFileExists("shadowfile",".");

    ArrayList<String> shadowFileList= new ArrayList<String>();
    if(shadowfileExists){
      shadowFileList=FileIO.loadFileToList("shadowfile");
      System.out.print("keep existing shadowfile? Y/n: ");
      String toDelete=inFromUser.readLine();
      if(Objects.equals(toDelete,"n")){
        shadowFileList= new ArrayList<String>();
        FileIO.deleteFile("shadowfile");
      }
    }
    shadowfileExists=FileIO.checkFileExists("shadowfile",".");

    if(!shadowfileExists){

      System.out.println("please input new users. Stop entering users by entering \".done\" without quotes");
      ArrayList<String> usernameList= new ArrayList<String>();
      ArrayList<String> passwordList= new ArrayList<String>();

        while(true){
          System.out.print("username: ");
          String username=inFromUser.readLine();
          if(Objects.equals(username,".done")){
            break;
          }
          System.out.print("password: ");
          String password=inFromUser.readLine();

          if(Objects.equals(password,".done")){
            System.out.println(username+" not added");
            break;
          }
          usernameList.add(username);
          passwordList.add(password);
        }
        for (int i=0;i<usernameList.size();i++ ) {
          String salt=generateSalt();
          shadowFileList.add(usernameList.get(i)+" "+salt+" "+hash_md5(passwordList.get(i),salt));
          FileIO.saveListToFile(shadowFileList,"shadowfile");
        }

      }




  }

  public static String hash_md5(String input,String salt){
    String output=null;
    if(input==null)return output;
    try {
      MessageDigest messageDigest =  MessageDigest.getInstance("MD5");
      String salted_input=input+salt;
      messageDigest.update(salted_input.getBytes(),0,salted_input.length());
      output=new BigInteger(1, messageDigest.digest()).toString(16);
    }catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return output;

  }

  public static Boolean login(String username, String password){

    ArrayList<String> shadowFileList=FileIO.loadFileToList("shadowfile");
    for (String shadowfileEntry : shadowFileList) {
      String[] splitShadowfileEntry = shadowfileEntry.split("\\s+");

      if(Objects.equals(splitShadowfileEntry[0],username)
          &&Objects.equals(splitShadowfileEntry[2],hash_md5(password,splitShadowfileEntry[1]))){
            return true;
      }
    }
    return false;
  }
}
