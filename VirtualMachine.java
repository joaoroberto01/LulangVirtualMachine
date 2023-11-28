import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class VirtualMachine {

    private static final Stack<Integer> memorySegment = new Stack<>();
    private static int s, pc = 0;

    private static List<Instruction> codeSegment;
    public static void initialize(String filepath) throws IOException {
        String code = readFileAsString(filepath);
        codeSegment = preProcess(code);

        boolean executing;
        do {
            Instruction instruction = codeSegment.get(pc++);
            executing = execute(instruction);
        } while (executing);
    }

    private static boolean execute(Instruction instruction) {
        int value, n;
        switch (instruction.name) {
            case "START":
                s = -1;
                break;
            case "HLT":
                return false;
            case "LDC":
                s++;
                value = Integer.parseInt(instruction.operand1);
                memorySegment.set(s, value);
                break;
            case "LDV":
                s++;
                value = memorySegment.get(Integer.parseInt(instruction.operand1));
                memorySegment.set(s, value);
                break;
            case "ADD":
                memorySegment.set(s - 1, memorySegment.get(s - 1) + memorySegment.get(s));
                s--;
                break;
            case "SUB":
                memorySegment.set(s - 1, memorySegment.get(s - 1) - memorySegment.get(s));
                s--;
                break;
            case "MULT":
                memorySegment.set(s - 1, memorySegment.get(s - 1) * memorySegment.get(s));
                s--;
                break;
            case "DIV":
                memorySegment.set(s - 1, memorySegment.get(s - 1) / memorySegment.get(s));
                s--;
                break;
            case "INV":
                value = -memorySegment.get(s);
                memorySegment.set(s, value);
                break;
            case "AND":
                value = memorySegment.get(s - 1) == 1 && memorySegment.get(s) == 1 ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "OR":
                value = memorySegment.get(s - 1) == 1 || memorySegment.get(s) == 1 ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "NEG":
                memorySegment.set(s, 1 - memorySegment.get(s));
                break;
            case "CME":
                value = memorySegment.get(s - 1) < memorySegment.get(s) ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "CMA":
                value = memorySegment.get(s - 1) > memorySegment.get(s) ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "CEQ":
                value = memorySegment.get(s - 1).equals(memorySegment.get(s)) ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "CDIF":
                value = memorySegment.get(s - 1).equals(memorySegment.get(s)) ? 0 : 1;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "CMEQ":
                value = memorySegment.get(s - 1) <= memorySegment.get(s) ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "CMAQ":
                value = memorySegment.get(s - 1) >= memorySegment.get(s) ? 1 : 0;
                memorySegment.set(s - 1, value);
                s--;
                break;
            case "STR":
                int address = Integer.parseInt(instruction.operand1);
                memorySegment.set(address, memorySegment.get(s));
                s--;
                break;
            case "JMP":
                pc = Integer.parseInt(instruction.operand1);
                break;
            case "JMPF":
                if (memorySegment.get(s) == 0) {
                    pc = Integer.parseInt(instruction.operand1);
                }
                //TODO senao pc = pc + 1
                s--;
                break;
            case "RD":
                Scanner scanner = new Scanner(System.in);
                value = scanner.nextInt();

                s++;
                memorySegment.add(s, value);
                break;
            case "PRN":
                System.out.println(memorySegment.get(s));
                s--;
                break;
            case "ALLOC":
                n = Integer.parseInt(instruction.operand2);
                for (int i = 0; i < n; i++) {
                    s++;
                    value = Integer.parseInt(instruction.operand1);
                    memorySegment.add(s);//;, memorySegment.get(value + i));
                    //FIXME vai dar merda
                }
                break;
            case "DALLOC":
                n = Integer.parseInt(instruction.operand2);
                for (int i = n - 1; i >= 0; i--) {
                    value = Integer.parseInt(instruction.operand1);
                    memorySegment.set(value + i, memorySegment.get(s));
                    s--;
                }
                break;
            case "CALL":
                s++;
                memorySegment.add(s, pc++);
                pc = Integer.parseInt(instruction.operand1);
                break;
            case "RETURN":
                pc = memorySegment.get(s);
                s--;
                break;
        }

        return true;
    }

    private static List<Instruction> preProcess(String code) {
        String[] lines = code.split("\n");

        List<Instruction> instructions = Instruction.fillInstructions(lines);
        int i = 0;
        for (Instruction instruction : instructions) {
            if (!instruction.label.isBlank()) {
                replaceLabel(instructions, instruction, i);
            }

            i++;
        }

        return instructions;
    }

    private static void replaceLabel(List<Instruction> instructions, Instruction replaceInstruction, int address) {
        for (Instruction instruction : instructions) {
            if (instruction.name.trim().equals("JMP") || instruction.name.trim().equals("JMPF") || instruction.name.trim().equals("CALL")) {
                instruction.operand1 = instruction.operand1.replace(replaceInstruction.label.trim(), String.valueOf(address));
            }
        }
        replaceInstruction.label = String.valueOf(address);
    }

    private static String readFileAsString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
