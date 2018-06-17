package controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FileHandler {

  
  
  public static void createFile(String path, String content) throws IOException {
    Writer out = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(path), "UTF-8"));
    try {
        out.write(content);
    } finally {
        out.close();
    }
  }
  public static String[] getContent (String path) {
    String builder="";
    String[] output = new String[2];
    try {
      File fileDir = new File(path);
          
      BufferedReader in = new BufferedReader(
         new InputStreamReader(
                    new FileInputStream(fileDir), "UTF8"));
          
      
      String str;
      
            
      while ((str = in.readLine()) != null) {
          builder+=str;
      }
      
      in.close();
      
      output[0] = builder;
      output[1] = fileDir.getName();
      
      } 
      catch (UnsupportedEncodingException e) 
      {
          System.out.println(e.getMessage());
      } 
      catch (IOException e) 
      {
          System.out.println(e.getMessage());
      }
      catch (Exception e)
      {
          System.out.println(e.getMessage());
      }
    
    return output;
  }

}
