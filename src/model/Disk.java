package model;

public class Disk {

  private int num_sectors;
  private int sector_size;
  private Folder root;
  
  public Disk(int num_sectors, int sector_size, String name_root) {
    this.setNum_sectors(num_sectors);
    this.setSector_size(sector_size);
    this.setRoot(new Folder(name_root, 0, null));
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
  
}
