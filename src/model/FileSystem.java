package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import controller.FileHandler;

public class FileSystem {

  private List<Disk> disks;
  private Disk current_disk;
  private Folder current_folder;
  
  public FileSystem() {
    
    this.disks = new ArrayList<Disk>();

  }
  
  private boolean diskAlreadyExists(String name) {
    for(Disk disk : this.disks) {
      if (disk.getRoot().getName() == name) {
        return true;
      }
    }
    return false;
    
  }

  public Folder getCurrent_folder() {
    return current_folder;
  }

  public void setCurrent_folder(Folder current_folder) {
    this.current_folder = current_folder;
  }
  
  public void CRT(int num_sectors, int sector_size, String root_name) {
    if (diskAlreadyExists(root_name)) {
      System.out.println("Duplicated name");
    } else {
      this.disks.add(new Disk(num_sectors, sector_size, root_name));
      this.setCurrent_folder(disks.get(disks.size()-1).getRoot());
      this.setCurrent_disk(disks.get(disks.size()-1));
    }
  }
  
  public void MKDIR(String name) {
    this.current_folder.MKDIR(name);
  }
  
  public int CHDIR(String name) {
    
    String folder_name = null;
    String[] paths = null;
    if (name.contains(":")) {
      if (name.contains(this.current_disk.getRoot().getName())) {
        this.setCurrent_folder(this.current_disk.getRoot());
        
        String[] paths_abs = name.split(":/", 2);
        if (paths_abs.length <= 1) {
          return 0;
        }
        String toSplit = paths_abs[1];
        paths = toSplit.split("/",2);
        folder_name = paths[0];
      }
      else {
        
        System.out.println("Path doesn't exists.");
        return -1;
      }
      
    }
    else {
      paths = name.split("/", 2);
      folder_name = paths[0];
    }
    
    if (folder_name.equals("..")) {
      if (current_folder.getLevel() > 0) {
        this.current_folder = current_folder.getParent();
      }
    } else {
      if (current_folder.findFolder(folder_name) != null) {
        this.current_folder = current_folder.findFolder(folder_name);
      } else {
        System.out.println("Folder doesn't exists.");
        return -1;
      }
    }
    if (paths.length>1) {
      String next_folder = paths[1];
      if (!next_folder.equals("")) {
        CHDIR(next_folder);
      }
    }
    return 0;
    
  
  }
  
  public void LDIR() {
    System.out.println("Folder(s):");
    for(Folder folder : this.current_folder.getFolders()) {
      System.out.println(folder.getName());
    }
    System.out.println("");
    System.out.println("File(s):");
    for(File file : this.current_folder.getFiles()) {
      System.out.println(file.getName()+"."+file.getExtension());
    }
    System.out.println("");
  }

  public void PPT(String file_name, String extension) {
    String output = current_folder.PPT(file_name, extension);
    System.out.println(output);
  }

  public void FLE(String file_name, String extension, String content) {
    this.current_folder.FLE(file_name, extension, content);
    
  }

  public void VIEW(String file_name, String extension) {
    String output = this.current_folder.VIEW(file_name,extension);
    System.out.println(output);
  }


  public void REM(String file_name, String extension) {
    this.current_folder.REM(file_name, extension, this.current_folder);
    
  }
    
  public Disk getCurrent_disk() {
    return current_disk;
  }

  public void setCurrent_disk(Disk current_disk) {
    this.current_disk = current_disk;
  }

  public void MOV_FILE(String file_name, String file_extension, String folder_mov, String new_file_name, String new_extension) {
    
    File file = findFile(file_name, file_extension, this.getCurrent_folder());
    Scanner reader = new Scanner(System.in);
    
    if(file!=null) {
      String abs_path = this.getCurrent_folder().getPath();
      if(this.CHDIR(folder_mov) == -1) {
        return;
      };
      
      String prev_name = file.getName();
      String prev_ext = file.getExtension();
      File collision = this.findFile(new_file_name, new_extension, this.getCurrent_folder());
      file.setName(new_file_name);
      file.setExtension(new_extension);
      if(collision!=null) {
        System.out.println("Filename already exists. Do you want to replace it? Y/N");
        String cmd = reader.nextLine();
        if(cmd.toLowerCase().equals("y")) {
          
          if(!abs_path.equals(getCurrent_folder().getPath())) {
            this.getCurrent_folder().REM(new_file_name, new_extension, this.getCurrent_folder());
          }
          
          this.getCurrent_folder().getFiles().add(file);
  
          file.setParent_folder(this.getCurrent_folder());
          file.updateDate();
          file.updateLevel();
          file.setPath();
          
          System.out.println("File moved.");
          this.CHDIR(abs_path);
          this.getCurrent_folder().getFiles().remove(file);
        
        } else {
          file.setName(prev_name);
          file.setExtension(prev_ext);
          System.out.println("File couldn't be created.");
        }      
      } else {
        this.getCurrent_folder().getFiles().add(file);        
        file.setParent_folder(this.getCurrent_folder());
        file.updateDate();
        file.updateLevel();
        file.setPath();
        System.out.println("File moved.");
        this.CHDIR(abs_path);
        this.getCurrent_folder().getFiles().remove(file);
      }
      this.CHDIR(abs_path);
    
    }
  }
  
  public void MOV_FOLDER(String folder_src, String folder_dest, String new_name) {
    
    if(folder_src.equals(this.getCurrent_disk().getRoot().getName())) {
      System.out.println("Cannot move the root folder.");
      return;
    }
    Folder folder = findFolder(folder_src, this.getCurrent_folder());
    Scanner reader = new Scanner(System.in);
    
    if(folder!=null) { 
      String abs_path = this.getCurrent_folder().getPath();
      if(this.CHDIR(folder_dest) == -1) {
        return;
      }
      
      String prev_name = folder.getName();
      Folder collision = this.findFolder(new_name, this.getCurrent_folder());
      folder.setName(new_name);
      if( collision != null) {
        System.out.println("Folder already exists. Do you want to replace it? Y/N");
        String cmd = reader.nextLine();
        if(cmd.toLowerCase().equals("y")) {
          
          if(!abs_path.equals(getCurrent_folder().getPath())) {
            this.REM(new_name, "folder");
          }
          this.getCurrent_folder().getFolders().add(folder);
  
          
          folder.setParent(this.getCurrent_folder());
          folder.setPath();
          folder.updateDate();
          folder.updateLevel();
          
          System.out.println("Folder moved.");
          this.CHDIR(abs_path);
          this.getCurrent_folder().getFolders().remove(folder);
              
         
          
        } else {
          folder.setName(prev_name);
          System.out.println("Folder couldn't be moved.");
        }      
      } else {
        this.getCurrent_folder().getFolders().add(folder);
        folder.setParent(this.getCurrent_folder());
        folder.setPath();
        folder.updateDate();
        folder.updateLevel();
        System.out.println("Folder moved.");
        this.CHDIR(abs_path);
        this.getCurrent_folder().getFolders().remove(folder);
      }
      this.CHDIR(abs_path);
    }
  }
  
  private Folder findFolder(String folder_src, Folder parent) {
    for(Folder folder : parent.getFolders()) {
      if(folder.getName().equals(folder_src)) {
        return folder;
      }
    }
    return null;
  }

  public File findFile(String file_name, String file_extension, Folder folder) {
    for(File file : folder.getFiles()) {
      if(file.getName().equals(file_name) && file.getExtension().equals(file_extension)) {
        return file;
      }
    }
    return null;
  }

  public void TREE(Folder parent) {
    
    for (Folder folder: parent.getFolders()) {
      System.out.println(new String(new char[folder.getLevel()]).replace("\0", "--")+"> "+"<FOLDER> "+folder.getName());
      this.TREE(folder);
    }
    for (File file : parent.getFiles()) {
      System.out.println(new String(new char[file.getLevel()]).replace("\0", "--")+"> "+"<FILE> "+file.getName()+"."+file.getExtension());
    }
    
  }

  public void MFLE(String file_name, String extension, String new_content) {
  
    File file = this.findFile(file_name, extension, this.getCurrent_folder());
    if (file==null) {
      System.out.println("File doesn't exists");
      return;
    }
    String old_content = file.getContent();
    Date old_mod_date = file.getModification_date();
    Date old_date = file.getCreation_date();
    
    file.REM();
    this.getCurrent_folder().getFiles().remove(file);
    File new_file = new File(file_name, extension, new_content, this.getCurrent_disk(), this.getCurrent_folder());
    if (new_file.isValid()){
      System.out.println("File modified");
      new_file.setCreation_date(old_date);
      this.getCurrent_folder().getFiles().add(new_file);
    } else {
      System.out.println("There is not enough available disk space.");
      new_file = new File(file_name, extension, old_content, this.getCurrent_disk(), this.getCurrent_folder());
      new_file.setCreation_date(old_date);
      new_file.setModification_date(old_mod_date);
      this.getCurrent_folder().getFiles().add(new_file);
    }
  }

  public void FIND(Folder parent, String name, String extension) {
    if (extension.equals("folder")){
      this.FIND_FOLDER(parent, name);
    }
    else {
      for (Folder folder: parent.getFolders()) {
        this.FIND(folder, name, extension);
      }
      for (File file : parent.getFiles()) {
        file.setPath();
        if (name.equals("*")){
          if(file.getExtension().equals(extension)) {
           
            System.out.println(file.getPath());
          }
        } else {
          if(file.getName().equals(name) && file.getExtension().equals(extension)) {
            System.out.println(file.getPath());
          }
        }  
      }
    }
  }

  private void FIND_FOLDER(Folder parent, String name) {
    for (Folder folder : parent.getFolders()) {
      folder.setPath();
      if(folder.getName().equals(name)) {
        System.out.println(folder.getPath());
      }
      this.FIND_FOLDER(folder, name);
    }
  }

  public void CPYRV(String real, String virtual) {
    String[] output = FileHandler.getContent(real);
    String content = output[0] ;
    String[] file_name = output[1].split("\\.");
    
    String abs_path = this.getCurrent_folder().getPath();
    if(this.CHDIR(virtual) >= 0) {
      this.FLE(file_name[0], file_name[1], content);
    }
    }


  public void CPYVR(String virtual_f, String virtual_e, String real) {
    File file = this.findFile(virtual_f, virtual_e, this.current_folder); 
    if(file == null) {
      System.out.println("File doesn't exists.");
      return;
    }
    
    String filename = virtual_f+"."+virtual_e;
    String path = real+"/"+filename;
    if (real.substring(real.length() - 1).equals("/")){
      path = real+filename;
    }
    try {
      FileHandler.createFile(path, file.getContent());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
        
    }
    
  public void CPYVV(String virtual_f, String virtual_e, String virtual) {
    File file = this.findFile(virtual_f, virtual_e, this.current_folder); 
    if(file == null) {
      System.out.println("File doesn't exists.");
      return;
    }
    
    String abs_path = this.getCurrent_folder().getPath();
    if(this.CHDIR(virtual) >= 0) {
      this.FLE(file.getName(), file.getExtension(), file.getContent());
    }
    }
    
  

  

}
