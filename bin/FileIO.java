import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.FileOutputStream;

public final class FileIO{

  private FileIO(){}
  // this pair of save and load functions i took from MY las project, slightly edited
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

  // in the following functions, i make sure that folderName is always hardcoded, and not user inputable. this protects the files outside the that folder
  public static void saveByteArrayToFile (byte[] byteArray,String fileName,String folderName) throws IOException{
    Path pathToFile = Paths.get("./"+folderName+"/"+fileName).toAbsolutePath();
    if(!pathToFile.startsWith(Paths.get(".").toAbsolutePath())) throw new IOException();

    Files.createDirectories(pathToFile.getParent());
    deleteFile(pathToFile.toAbsolutePath().toString());
    Files.createFile(pathToFile);
    FileOutputStream fileOutputStream = new FileOutputStream("./"+folderName+"/"+fileName);
    fileOutputStream.write(byteArray);
    fileOutputStream.close();
  }

  public static byte[] loadFileToByteArray(String fileName, String folderName) throws IOException{
      Path loadPath = Paths.get("./"+folderName+"/"+ fileName).toAbsolutePath();
      if(!loadPath.startsWith(Paths.get("./"+folderName).toAbsolutePath())) throw new IOException();
      return Files.readAllBytes(loadPath);
  }

  public static Boolean deleteFile(String filepath){
    return (new File(filepath).delete());
  }

  public static Boolean checkFileExists(String fileName,String folderName) throws IOException{
    Path loadPath = Paths.get("./"+folderName+"/"+ fileName).toAbsolutePath();
    if(!loadPath.startsWith(Paths.get("./"+folderName).toAbsolutePath())) throw new IOException();
    File file = new File(loadPath.toString());
    return file.exists() && !file.isDirectory();
  }
}
