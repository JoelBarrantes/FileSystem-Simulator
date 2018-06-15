package model;
import java.util.Date;

public class File {
  
  private String name;
  private String extension;
  private Date creation_date;
  private Date modification_date;
  private int size;
  private String content;
  
  public File(String name, String extension, String content ) {
    this.setName(name);
    this.setExtension(extension);
    this.setContent(content);
    this.setCreation_date(new Date());
    this.setModification_date(new Date());
   
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
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

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  
}
