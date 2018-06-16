package model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Sector {
  
  private static int identity = 0;
  private int sector_id;
  private String content;
  private int size;
  private int used_size;
  private boolean isFree;
  private int previous_sector;
  
  public Sector(int size, int previous_sector) {
    
    this.setSector_id(getIdentity());
    setIdentity(getIdentity() + 1);
    this.setSize(size);
    byte[] content = new byte[this.getSize()];
    Arrays.fill(content,(byte)35);
    String default_string = new String(content, StandardCharsets.UTF_8);
    this.setContent(default_string);
    this.setUsed_size(0);
    this.setPrevious_sector(previous_sector);
    this.setFree(true);
    
  }
  
  public void reset() {
    byte[] content = new byte[this.getSize()];
    Arrays.fill(content,(byte)35);
    String default_string = new String(content, StandardCharsets.UTF_8);
    this.setContent(default_string);
    this.setUsed_size(0);
    this.setFree(true);
  }

  public static int getIdentity() {
    return identity;
  }

  public static void setIdentity(int identity) {
    Sector.identity = identity;
  }

  public int getSector_id() {
    return sector_id;
  }

  public void setSector_id(int sector_id) {
    this.sector_id = sector_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getUsed_size() {
    return used_size;
  }

  public void setUsed_size(int used_size) {
    this.used_size = used_size;
  }

  public int getPrevious_sector() {
    return previous_sector;
  }

  public void setPrevious_sector(int previous_sector) {
    this.previous_sector = previous_sector;
  }

  public boolean isFree() {
    return isFree;
  }

  public void setFree(boolean isFree) {
    this.isFree = isFree;
  }
  
}
