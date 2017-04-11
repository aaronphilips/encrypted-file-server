import java.util.ArrayList;
import java.util.Iterator;
// import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.FileOutputStream;
// http://www.programcreek.com/java-api-examples/index.php?class=org.apache.commons.io.FileUtils&method=writeByteArrayToFile
// http://stackoverflow.com/questions/2833853/create-whole-path-automatically-when-writing-to-a-new-file
public final class FileIO{

  private FileIO(){}

  public static void saveListToFile (ArrayList<?> listToBePrinted,String fileName){
    Iterator<?> listToBePrintedIterator=listToBePrinted.iterator();

    try(FileWriter writer = new FileWriter( fileName)){
      while(listToBePrintedIterator.hasNext()){
        writer.write(listToBePrintedIterator.next().toString()+System.getProperty("line.separator"));
      }
    }catch(IOException e){
      e.printStackTrace();
    };
  }

  public static ArrayList<String> loadFileToList(String fileName){
    ArrayList<String> stringList = new ArrayList<String>();
    try(Scanner s = new Scanner(new File("./"+fileName)).useDelimiter(System.lineSeparator())){
      while (s.hasNext()){ stringList.add(s.next());}
    }
    catch(FileNotFoundException e){
      e.printStackTrace();
      System.out.println("File:<"+fileName+"> was not found");
    }
    return stringList;
  }

  public static void saveByteArrayToFile (byte[] byteArray,String fileName,String username){
    try{
      Path pathToFile = Paths.get("./"+username+"/"+fileName);
      Path parent = Paths.get(".").toAbsolutePath();
      System.out.println(parent.toString());
      Files.createDirectories(pathToFile.getParent());
      Files.createFile(pathToFile);
    }catch(IOException e){
      e.printStackTrace();
    };


    try(FileOutputStream fileOutputStream = new FileOutputStream("./"+username+"/"+fileName)){
      fileOutputStream.write(byteArray);
      // FileUtils.writeByteArrayToFile(new File("./"+username+"/"+fileName), byteArray);
    }catch(IOException e){
      e.printStackTrace();
    };
  }
  // http://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist
  // this will be hardcoded to data, so less input checking
  public static byte[] loadFileToByteArray(String fileName) throws IOException{
      return Files.readAllBytes(Paths.get("./"+"DATA/"+ fileName));
  }

  public static Boolean deleteFile(String fileName){
    return (new File("./"+fileName).delete());
  }

  public static void createDATA(){
    try{
      Files.createDirectories(Paths.get("./DATA"));
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    try {
      byte[] byteArray= loadFileToByteArray("input.txt");
      saveByteArrayToFile(byteArray,"output.txt","user");
    }catch (Exception e) {
      System.out.println("wentbad");
    }

  }
}
