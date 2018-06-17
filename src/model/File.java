package model;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class File {
  
  private String name;
  private String extension;
  private Date creation_date;
  private Date modification_date;
  private int size;
  private int disk_size;
  private String content;
  private Disk parent_disk;
  private List<Sector> sectors;
  private Folder parent_folder;
  private boolean isValid;
  private int level;
  private String path;
  
  public File(String name, String extension, String content, Disk parent_disk, Folder parent_folder ) {
    this.setName(name);
    this.setExtension(extension);
    this.setContent(content);
    this.setCreation_date(new Date());
    this.setModification_date(new Date());
    this.setParent_disk(parent_disk);
    this.setParent_folder(parent_folder);
    this.setSize(0);
    this.setDisk_size(0);
    this.setSectors(new ArrayList<Sector>());
    this.setValid(false);
    int len_bytes = this.writeOnDisk();
    if (len_bytes >= 0) {
      this.setValid(true);
    }
    this.updateLevel();
    this.setPath();
    
  }
  
  public int writeOnDisk() {
    
    int sector_size = parent_disk.getSector_size();

    int len_bytes = content.getBytes(StandardCharsets.UTF_8).length;  
    
    int required_sectors = (int) Math.ceil((double) len_bytes/sector_size);
    if (required_sectors == 0) {
      required_sectors++;
    }
    
    byte[] bytes = content.getBytes(StandardCharsets.UTF_8);  
    
    if (required_sectors > this.getParent_disk().getFreeSectors()) {
      return -1;
    } else {
      
     int prev_sector = 0;
     for(int i=0; i < required_sectors; i++ ) {
       int written_size = sector_size;
       int index = i * sector_size;
       byte[] content = new byte[sector_size];
       Arrays.fill(content,(byte)35);
       for(int j = 0; j < sector_size; j++) {
         if (index+j >= len_bytes) {
           written_size = j;
           break;
         } else {
           content[j]=bytes[index+j];
         } 
       }
       String sector_content = new String(content, StandardCharsets.UTF_8);
       Sector sector = this.getParent_disk().findNextSector();
       
       if(i == 0) {
         prev_sector = sector.getSector_id();
         sector.setPrevious_sector(prev_sector);
       } else { 
         sector.setPrevious_sector(prev_sector);
         prev_sector = sector.getSector_id();        
       }
       
       sector.setFree(false);
       sector.setContent(sector_content);
       sector.setUsed_size(written_size);
       this.getSectors().add(sector);
     } 
    
    }
    this.setSize(len_bytes);
    this.setDisk_size(required_sectors*sector_size);
    return len_bytes;
    
  }
  
  public void REM() {
    for (Sector sector : this.getSectors()) {
      sector.reset();
    }
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

  public String getProperties() {
    // TODO Auto-generated method stub
    String builder = "";
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
    builder += "Name: "+this.getName()+"\n"+
               "Extension: "+this.getExtension()+"\n"+

               "Creation Date: "+df.format(this.creation_date)+"\n"+

               "Last Modified Date: "+df.format(this.modification_date)+"\n"+

               "Size (In bytes): "+String.valueOf(this.size)+"\n"+
               "Size on disk (In bytes): "+String.valueOf(this.disk_size)+"\n";

    
    
    return builder;
  }

  public Disk getParent_disk() {
    return parent_disk;
  }

  public void setParent_disk(Disk parent_disk) {
    this.parent_disk = parent_disk;
  }

  public List<Sector> getSectors() {
    return sectors;
  }

  public void setSectors(List<Sector> sectors) {
    this.sectors = sectors;
  }

  public int getDisk_size() {
    return disk_size;
  }

  public void setDisk_size(int disk_size) {
    this.disk_size = disk_size;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean isValid) {
    this.isValid = isValid;
  }

  public Folder getParent_folder() {
    return parent_folder;
  }

  public void setParent_folder(Folder parent_folder) {
    this.parent_folder = parent_folder;
  }
  
  public void updateDate() {
    this.setModification_date(new Date());
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }
  
  public void updateLevel() {
    this.setLevel(parent_folder.getLevel()+1);
  }

  public String getPath() {
    return path;
  }

  public void setPath() {
    if (level > 0) {
      this.path = this.getParent_folder().getPath() + "/" +this.name+"."+this.extension;
    } else {
      this.path = this.name;
    }
    
  }
}
