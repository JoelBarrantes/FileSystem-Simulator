package view;

import java.util.Scanner;
import controller.CmdParser;

public class CommandPrompt {

  private String instruction;
  private Scanner reader;
  private CmdParser cmd_parser;

  public CommandPrompt() {
    
    this.instruction="";
    this.cmd_parser = new CmdParser();
    System.out.print("? - ");
    this.setReader(new Scanner(System.in));
    this.initLoop();
    

  }
  
  private void initLoop() {
    
    while(true) {
      getInstruction();
      if(instruction.toLowerCase().equals("exit")) {
        break; 
      }
      parseInstruction(); 
    }
    
  }
  
  private void parseInstruction() {
    this.cmd_parser.parseCommand(this.instruction);
    
  }
  
  private void getInstruction() {
    String cmd = this.reader.nextLine();
    this.setInstruction(cmd);
  }
  
  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public Scanner getReader() {
    return reader;
  }

  public void setReader(Scanner reader) {
    this.reader = reader;
  }
  
}
