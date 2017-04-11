import java.util.ArrayList;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public final class FileIO{

  private FileIO(){}

  public static void saveListToFile (ArrayList<?> listToBePrinted,String fileName){
    Iterator<?> listToBePrintedIterator=listToBePrinted.iterator();

    try(PrintWriter writer = new PrintWriter(  fileName,"UTF-8" )){
      while(listToBePrintedIterator.hasNext()){
        writer.println(listToBePrintedIterator.next().toString());
      }
    }catch(IOException e){
      e.printStackTrace();
    };
  }
  // http://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist
  public static ArrayList<String> loadFileToList(String fileName){
    ArrayList<String> stringList = new ArrayList<String>();
    try(Scanner s = new Scanner(new File("./"+fileName))){
      while (s.hasNext()){ stringList.add(s.next());}
    }
    catch(FileNotFoundException e){
      e.printStackTrace();
      System.out.println("File:<"+fileName+"> was not found");
    }
    return stringList;
  }

  public static Boolean deleteFile(String fileName){
    return (new File("./"+fileName).delete());
  }
}
