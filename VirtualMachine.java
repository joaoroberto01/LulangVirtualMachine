import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

public class VirtualMachine {
    private static String code;

    private static Stack<Integer> memorySegment;

    private static List<Instruction> codeSegment;
    public static void initialize(String filepath) throws IOException {
        code = readFileAsString(filepath);

        codeSegment = preProcess(code);
        System.out.println();
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
            if (instruction.name.trim().matches("\\b(JMP|JMPF|CALL)\\b")) {
                instruction.operand1 = instruction.operand1.replace(replaceInstruction.label.trim(), String.valueOf(address));
            }
        }
        replaceInstruction.label = String.valueOf(address);
    }

    private static String readFileAsString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
