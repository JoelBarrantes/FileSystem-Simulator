package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class FileSystem {

  private List<Disk> disks;
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
    }
  }
  
  public void MKDIR(String name) {
    this.current_folder.MKDIR(name);
  }
  
  public void CHDIR(String name) {
    if (name.equals("..")) {
      if (current_folder.getLevel() > 0) {
        this.current_folder = current_folder.getParent();
      }
    } else {
      if (current_folder.findFolder(name) != null) {
        this.current_folder = current_folder.findFolder(name);
      } else {
        System.out.println("Folder doesn't exists.");
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
}
