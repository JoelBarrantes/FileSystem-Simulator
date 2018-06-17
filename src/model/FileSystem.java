package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


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
  
  public void CHDIR(String name) {
    
    String folder_name = null;
    String[] paths = null;
    if (name.contains(":")) {
      if (name.contains(this.current_disk.getRoot().getName())) {
        this.setCurrent_folder(this.current_disk.getRoot());
        
        String[] paths_abs = name.split(":/", 2);
        if (paths_abs.length <= 1) {
          return;
        }
        String toSplit = paths_abs[1];
        paths = toSplit.split("/",2);
        folder_name = paths[0];
      }
      else {
        
        System.out.println("Path doesn't exists.");
        return;
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
      }
    }
    if (paths.length>1) {
      String next_folder = paths[1];
      if (!next_folder.equals("")) {
        CHDIR(next_folder);
      }
    }
    
  
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
      this.CHDIR(folder_mov);
      
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
        System.out.println("File moved.");
        this.CHDIR(abs_path);
        this.getCurrent_folder().getFiles().remove(file);
      }
      this.CHDIR(abs_path);
    
    }
  }
  
  public void MOV_FOLDER(String folder_src, String folder_dest, String new_name) {
    
    Folder folder = findFolder(folder_src, this.getCurrent_folder());
    Scanner reader = new Scanner(System.in);
    
    if(folder!=null) { 
      String abs_path = this.getCurrent_folder().getPath();
      this.CHDIR(folder_dest);
      
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
      
    }
    for (File file : parent.getFiles()) {
      
    }
    
  }


}
