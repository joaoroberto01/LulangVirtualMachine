import java.util.ArrayList;
import java.util.List;

public class Instruction {
    public String label;
    public String name;
    public String operand1;
    public String operand2;

    public static List<Instruction> fillInstructions(String[] lines) {
        List<Instruction> instructions = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) continue;

            Instruction instruction = parse(line);
            instructions.add(instruction);
        }

        return instructions;
    }
    public static Instruction parse(String instructionString) {
        Instruction instruction = new Instruction();
        instruction.label = getInstructionRange(instructionString, 0, 4);
        instruction.name = getInstructionRange(instructionString, 4, 12);
        instruction.operand1 = getInstructionRange(instructionString, 12, 16);
        instruction.operand2 = getInstructionRange(instructionString, 16, 20);
        return instruction;
    }
    private static String getInstructionRange(String string, int beginIndex, int endIndex) {
        try {
            return string.substring(beginIndex, endIndex);
        } catch (Exception e) {
            if (beginIndex < string.length()) {
                return string.substring(beginIndex);
            }
            return "";
        }
    }

    @Override
    public String toString() {
        return String.format("%-4s%-8s%-4s%-4s\n", label, name, operand1, operand2);
    }
}
