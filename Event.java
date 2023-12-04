import com.google.gson.Gson;

import java.util.List;
import java.util.Stack;

public class Event {

    enum EventType {
        SETUP, FINISH, INPUT, OUTPUT, INSTRUCTION_FETCH, INSTRUCTION_EXECUTE
    }
    private EventType type;

    private Object content;

    public Event(EventType type) {
        this.type = type;
    }

    public Event(EventType type, Object content) {
        this.type = type;
        this.content = content;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static void notifyInstructionFetch(int pc) {
        Event event = new Event(Event.EventType.INSTRUCTION_FETCH, pc);
        notify(event);
    }

    public static void notifyInstructionExecute(Stack<Integer> memory) {
        Event event = new Event(EventType.INSTRUCTION_EXECUTE, memory);
        notify(event);
    }

    public static void notifyCodeSetup(List<Instruction> codeSegment) {
        Event event = new Event(Event.EventType.SETUP, codeSegment);
        notify(event);
    }

    public static void notifyFinish() {
        Event event = new Event(EventType.FINISH);
        notify(event);
    }

    public static void notifyInput() {
        Event event = new Event(EventType.INPUT);
        notify(event);
    }

    public static void notifyOutput(String content) {
        Event event = new Event(EventType.OUTPUT, content);
        notify(event);
    }

    private static void notify(Event event) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(event));
    }
}
