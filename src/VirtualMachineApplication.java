package src;

import java.nio.file.NoSuchFileException;

public class VirtualMachineApplication {

    public static  void main(String[] args){
        String filepath = args.length == 0 ? "x.obj" : args[0];
        try {
            VirtualMachine.initialize(filepath);
        } catch (NoSuchFileException e) {
            System.err.printf("file '%s' not found\n", filepath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
