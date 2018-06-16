package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Disk {

  private int num_sectors;
  private int sector_size;
  private String archive;
  private Folder root;
  private List<Sector> sectors;
  
  public Disk(int num_sectors, int sector_size, String name_root) {
    this.setNum_sectors(num_sectors);
    this.setSector_size(sector_size);
    this.setRoot(new Folder(name_root, 0, null, this));
    this.setSectors(new ArrayList<Sector>());
    this.formatDisk();
  }
  
  private void formatDisk() {
    
    sectors.add(new Sector(this.sector_size, num_sectors - 1));
    for(int i= 1; i < this.num_sectors; i++) {
      sectors.add(new Sector(this.sector_size, i-1));
    }
    this.updateDisk();
    
  }
  
  public void updateDisk() {
    String builder = "";
    for(Sector sector : sectors) {
      builder+=sector.getContent();
    }
    this.setArchive(builder);
    try {
      this.saveArchive();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public int getFreeSectors() {
    int free_sectors = 0;
    for (Sector sector : sectors) {
      if(sector.isFree()) {
        free_sectors++;
      } 
    }
    return free_sectors;
  }
  
  public void saveArchive() throws IOException {
    String path = "Disks/"+this.getRoot().getName().substring(0, this.getRoot().getName().length()-1)+".txt";
    Writer out = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(path), "UTF-8"));
    try {
        out.write(this.getArchive());
    } finally {
        out.close();
    }
  }
  
  public Sector findNextSector() {
    for (Sector sector : sectors) {
      if (sector.isFree()){
        return sector;
      }
    }
    return null;
    
  }
  public int getNum_sectors() {
    return num_sectors;
  }

  public void setNum_sectors(int num_sectors) {
    this.num_sectors = num_sectors;
  }

  public int getSector_size() {
    return sector_size;
  }

  public void setSector_size(int sector_size) {
    this.sector_size = sector_size;
  }

  public Folder getRoot() {
    return root;
  }

  public void setRoot(Folder root) {
    this.root = root;
  }

  public List<Sector> getSectors() {
    return sectors;
  }

  public void setSectors(List<Sector> sectors) {
    this.sectors = sectors;
  }
  
  public String getArchive() {
    return archive;
  }

  public void setArchive(String archive) {
    this.archive = archive;
  }

}
