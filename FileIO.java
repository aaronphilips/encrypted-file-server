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

  public static void saveByteArrayToFile (byte[] byteArray,String fileName,String folderName) throws IOException{
    Path pathToFile = Paths.get("./"+folderName+"/"+fileName).toAbsolutePath();
    if(!pathToFile.startsWith(Paths.get(".").toAbsolutePath())) throw new IOException();

    Files.createDirectories(pathToFile.getParent());
    deleteFile(pathToFile.toAbsolutePath().toString());
    Files.createFile(pathToFile);
    FileOutputStream fileOutputStream = new FileOutputStream("./"+folderName+"/"+fileName);
    fileOutputStream.write(byteArray);



  }

  // http://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist
  public static byte[] loadFileToByteArray(String fileName, String folderName) throws IOException{
      Path loadPath = Paths.get("./"+folderName+"/"+ fileName).toAbsolutePath();
      if(!loadPath.startsWith(Paths.get("./"+folderName).toAbsolutePath())) throw new IOException();
      return Files.readAllBytes(loadPath);
  }

  public static Boolean deleteFile(String filepath){
    return (new File(filepath).delete());
  }

  // public static void createDATA(){
  //   try{
  //     Files.createDirectories(Paths.get("./DATA"));
  //   }catch (IOException e) {
  //     e.printStackTrace();
  //   }
  // }
  public static void main(String[] args) {
    try {
      byte[] byteArray= loadFileToByteArray("input.txt","DATA");
      saveByteArrayToFile(byteArray,"output.txt","user");
    // do something

    }catch (Exception e) {
      System.out.println("wentbad");
      e.printStackTrace();
    }

  }
}
