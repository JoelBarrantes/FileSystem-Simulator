package controller;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.FileSystem;
import model.Folder;



public final class CmdParser {
  
  private String current_folder;
  private FileSystem file_system ;
  private boolean init;
  
  public CmdParser() {
    this.file_system = new FileSystem();
    this.init = false;

  }
  
  public void parseCommand(String command) {
    
    List<String> args = splitCommand(command);
    String cmd = args.get(0);
    
    switch (cmd.toLowerCase()) {
      case "crt": //CREATE VIRTUAL DISK
        
        this.init = true;
        int num_sectors = Integer.parseInt(args.get(1));
        int sector_size = Integer.parseInt(args.get(2));
        String root_name = args.get(3);
        
        this.file_system.CRT(num_sectors, sector_size, root_name+":");
        
        break;
      
      case "fle": //CREATE FILE
        
        String[] name_ext = args.get(1).split("\\.");
        String content = args.get(2);
        this.file_system.FLE(name_ext[0], name_ext[1], content);
        
        break;
      
      case "mkdir": //CREATE FOLDER
        
        String name_folder = args.get(1);
        this.file_system.MKDIR(name_folder);
        break;
        
      case "chdir": //MOVE TO ANOTHER FOLDER
        
        String path = args.get(1);
        this.file_system.CHDIR(path);
        
        break;
        
      case "ldir": //LIST ALL FOLDERS AND FILES
        
        this.file_system.LDIR();
        break;
      
      case "mfle": //MODIFY FILE CONTENT
        
        //TODO implementation
        
        break;
        
      case "ppt": //PROPERTIES OF FILE
        String[] file_ppt = args.get(1).split("\\.");
        this.file_system.PPT(file_ppt[0], file_ppt[1]);
           
        break;
      
      case "view":
      
        String[] file_view = args.get(1).split("\\.");
        this.file_system.VIEW(file_view[0], file_view[1]);
        
        break;
      case "cpy":
        
        //TODO implementation
        
        break;
        
      case "mov":
        break;
      case "rem":
        break;
      case "tree":
        break;
      case "find":     
    }
    this.current_folder = file_system.getCurrent_folder().getPath();
    System.out.println(this.current_folder);
  }
  
  public static List<String> splitCommand(String command){
    List<String> args = new ArrayList<String>();
    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
    while (m.find())
        args.add(m.group(1).replace("\"", "")); // Add) to remove surrounding quotes.
    //System.out.println(args);
    return args;
    
  }
  
}