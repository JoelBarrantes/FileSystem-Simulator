package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Folder {
  
  private String name;
  private List<File> files;
  private List<Folder> folders;
  private Date creation_date;
  private Date modification_date;
  private int level;
  private Folder parent;
  private String path;
  private Disk parent_disk;
  private boolean isValid;
  
  
  public Folder(String name, int level, Folder parent, Disk parent_disk) {
    
    this.setName(name);
    this.setFiles(new ArrayList<File>());
    this.setFolders(new ArrayList<Folder>());
    this.setCreation_date(new Date());
    this.setModification_date(new Date());
    this.setLevel(level);
    this.setParent(parent);
    this.setParent_disk(parent_disk);
    this.setPath();
  }

  public void MKDIR(String name) {
    
    Scanner reader = new Scanner(System.in);
    Folder folder = new Folder(name, this.level+1 ,this, this.getParent_disk()); 
    Folder collision = checkFolderCollision(folder);
    
    if(collision != null) {
      System.out.println("Folder already exists. Do you want to merge it with this folder? Y/N");
      String cmd = reader.nextLine();
      if(cmd.toLowerCase().equals("y")) {
        this.MERGE(collision, folder);
        System.out.println("Folder created.");
      } else {
        System.out.println("Folder couldn't be created.");
      }     
    } else {
      this.folders.add(folder);
      System.out.println("Folder created.");
    }
  }
  
  private void MERGE(Folder destination, Folder source) {
    for(File file : source.getFiles()) {
      file.setParent_folder(destination);
      file.setParent_disk(destination.getParent_disk());
      destination.getFiles().add(file);
    }
    for(Folder folder : source.getFolders()) {
      folder.setParent(destination);
      folder.setParent_disk(destination.getParent_disk());
      destination.getFolders().add(folder);
    }
    source = null;
  }

  public void FLE(String file_name, String extension, String content) {
    
    Scanner reader = new Scanner(System.in);
    File new_file = new File(file_name, extension, content, parent_disk, this);
    if(new_file.isValid()){
      if(checkFileCollision(new_file)) {
        System.out.println("Filename already exists. Do you want to replace it? Y/N");
        String cmd = reader.nextLine();
        if(cmd.toLowerCase().equals("y")) {
          this.REM(file_name, extension, this);
          //new_file = new File(file_name, extension, content, parent_disk, this);
          this.files.add(new_file); 
          System.out.println("File created.");
        } else {
          new_file.REM();
          System.out.println("File couldn't be created.");
        }   
      } else {
        this.files.add(new_file); 
        System.out.println("File created.");
      }
    } else {
      System.out.println("There is not enough available disk space.");
    }
  }
  
  public boolean checkFileCollision(File new_file) {
    for (File file : this.files) {
      if(file.getName().equals(new_file.getName()) && file.getExtension().equals(new_file.getExtension())) {
        return true;
      }
    }
    return false;
  }
  
  public Folder checkFolderCollision(Folder new_folder) {
    for (Folder folder : this.folders) {
      if(folder.getName().equals(new_folder.getName())) {
        return folder;
      }
    }
    return null;
  }

  public String PPT(String file_name, String file_extension) {
    for(File file : this.getFiles()) {
      if (file.getName().equals(file_name) && file.getExtension().equals(file_extension)){
        return file.getProperties();
      }
    }
    return "File doesn't exists";
  }

  public String VIEW(String file_name, String extension) {
    for(File file : this.getFiles()) {
      if (file.getName().equals(file_name) && file.getExtension().equals(extension)){
        return file.getContent();
      }
    }
    return "File doesn't exists";
  }

  public void REM(String file_name, String extension, Folder folder) {
    
    if(extension.equals("folder")) {
      this.REM_FOLDER(file_name, folder);
    }
   
    for(Iterator<File> file = folder.getFiles().listIterator(); file.hasNext();) {
      File file_i = file.next();
      if (file_name.equals("*")) {
        if (file_i.getExtension().equals(extension)){
          file_i.REM();
          file.remove();
          System.out.println("File eliminated");
        }
      }
      else {
        if (file_i.getName().equals(file_name) && file_i.getExtension().equals(extension)){
          file_i.REM();
          file.remove();
          System.out.println("File eliminated");
        }
      }
    }
   }
   
  private void REM_FOLDER(String folder_name, Folder parent) {
    
    for(Iterator<Folder> folder = parent.getFolders().listIterator(); folder.hasNext();) {
      Folder folder_i = folder.next();
      if (folder_i.getName().equals(folder_name)){
         this.REM_RECURSIVE(folder_i);
         folder.remove();
      }
    }
  }
  
  private void REM_RECURSIVE(Folder parent) {
   
    for(Iterator<Folder> folder = parent.getFolders().listIterator(); folder.hasNext();) {
      Folder folder_i = folder.next();
      this.REM_RECURSIVE(folder_i);
    }
    for(Iterator<File> file = parent.getFiles().listIterator(); file.hasNext();) {
      File file_i = file.next();
      file_i.REM();
      file.remove();
      System.out.println("File eliminated");
    }
    
  }

  public Folder findFolder(String name) {
    for(Folder folder : folders) {
      if (folder.getName().equals(name)) {
        return folder;
      }
    }
    return null;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<File> getFiles() {
    return files;
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }

  public Date getCreation_date() {
    return creation_date;
  }

  public void setCreation_date(Date creation_date) {
    this.creation_date = creation_date;
  }

  public Date getModification_date() {
    return modification_date;
  }

  public void setModification_date(Date modification_date) {
    this.modification_date = modification_date;
  }

  public List<Folder> getFolders() {
    return folders;
  }

  public void setFolders(List<Folder> folders) {
    this.folders = folders;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public Folder getParent() {
    return parent;
  }

  public void setParent(Folder parent) {
    this.parent = parent;
  }

  public String getPath() {
    return path;
  }

  public void setPath() {
    if (level > 0) {
      this.path = this.parent.getPath() + "/" +this.name;
    } else {
      this.path = this.name;
    }
    
  }

  public Disk getParent_disk() {
    return parent_disk;
  }

  public void setParent_disk(Disk parent_disk) {
    this.parent_disk = parent_disk;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean isValid) {
    this.isValid = isValid;
  }

  public void updateDate() {
    this.setModification_date(new Date());
  }
  
  public void updateLevel() {
    this.setLevel(this.parent.getLevel()+1);
  }
}
