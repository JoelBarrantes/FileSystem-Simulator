package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Folder {
  
  private String name;
  private List<File> files;
  private List<Folder> folders;
  private Date creation_date;
  private Date modification_date;
  private int level;
  private Folder parent;
  private String path;
  
  public Folder(String name, int level, Folder parent) {
    
    this.setName(name);
    this.setFiles(new ArrayList<File>());
    this.setFolders(new ArrayList<Folder>());
    this.setCreation_date(new Date());
    this.setModification_date(new Date());
    this.setLevel(level);
    this.setParent(parent);
    this.setPath();
  }

  public void MKDIR(String name) {
    this.folders.add(new Folder(name, this.level+1 ,this));
  }
  
  public void FLE(String file_name, String extension, String content) {
    this.files.add(new File(file_name, extension, content));
  }
  
  public String PPT(String file_name, String file_extension) {
    for(File file : this.getFiles()) {
      if (file.getName().equals(file_name) && file.getExtension().equals(file_extension)){
        return file.getProperties();
      }
    }
    return "File doesn't exists";
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

  public String VIEW(String file_name, String extension) {
    for(File file : this.getFiles()) {
      if (file.getName().equals(file_name) && file.getExtension().equals(extension)){
        return file.getContent();
      }
    }
    return "File doesn't exists";
  }
   

}
