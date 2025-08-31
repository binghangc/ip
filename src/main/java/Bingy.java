import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import util.Parser;
import util.Parser.ParsedCommand;

class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public TaskManager(int size) {
    }

    public void markDone(int index) {
        tasks.get(index).markDone();
    }

    public void markUndone(int index) {
        tasks.get(index).markUndone();
    }

    public void deleteTask(int index) {
        tasks.remove(index);
    }

    public ToDo addToDo(String description) {
        ToDo task = new ToDo(description);
        tasks.add(task);
        return task;
    }

    public Deadline addDeadline(String description, LocalDate deadline) {
        Deadline task = new Deadline(description, deadline);
        tasks.add(task);
        return task;
    }

    public Events addEvents(String description, String start, String end) {
        Events task = new Events(description, start, end);
        tasks.add(task);
        return task;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public int getSize() {
        return this.getTasks().size();
    }

    public void addAll(List<Task> items) {
        if (items != null) {
            tasks.addAll(items);
        }
    }

}

class BingyBot {
    private static TaskManager taskManager = new TaskManager(100);
    private static final Storage storage = new Storage("tasks.txt");
    private final Ui ui = new Ui();
    private boolean running = true;


    public void run() {
        ui.greet();
        try {
            ArrayList<Task> loaded = storage.load();
            taskManager.addAll(loaded);
        } catch (IOException e) {
            ui.sendMessage("Starting fresh (no saved tasks found).");
        }
        Scanner sc = new Scanner(System.in);
        while (running) {
            try {
                String input = sc.nextLine();
                handleInput(input);
            } catch (Exception e) {
                ui.sendMessage(e.getMessage());
            }
        }
        sc.close();
    }


    private String listStatus() {
        int size = taskManager.getSize();
        String taskWord = (size == 1) ? "task" : "tasks";
        return String.format("Now you have %d %s in the list", size, taskWord);
    }


    private void handleInput(String input) throws EmptyTaskException {
        String trimmed = input.trim();

        ParsedCommand cmd = Parser.parseUserCommand(input);

        switch (cmd.type) {
            case BYE:
                sayGoodbye();
                running = false;
                return;

            case LIST:
                ui.showTasks(taskManager.getTasks());
                return;

            case MARK:
            case UNMARK:
            case DELETE:
                if (cmd.arg1 == null || cmd.arg1.isEmpty()) {
                    ui.sendMessage("What the helly. Give a valid input");
                    return;
                }
                int taskIndex;
                try {
                    taskIndex = Integer.parseInt(cmd.arg1) - 1; // to 0-based
                } catch (NumberFormatException e) {
                    ui.sendMessage(" Give me a task number. Numbers are characters like 1 or 2. Hope that helps!");
                    return;
                }
                List<Task> taskList = taskManager.getTasks();
                if (taskIndex < 0 || taskIndex >= taskList.size()) {
                    throw new InvalidTaskIndexException(
                            String.format("Index %d is out of bounds (valid range: 1 to %d)",
                                    taskIndex + 1, taskList.size())
                    );
                }

                Task t = taskList.get(taskIndex);
                if (cmd.type == ParsedCommand.Type.MARK) {
                    taskManager.markDone(taskIndex);
                    ui.sendMessage(" Hopefully you did the task properly... Marked it for you\n   " + t);
                    persist();
                    return;
                } else if (cmd.type == ParsedCommand.Type.UNMARK){
                    taskManager.markUndone(taskIndex);
                    ui.sendMessage(" Ha! I knew you couldn't do it. Unmarked it! Welcome\n   " + t);
                    persist();
                    return;
                } else if (cmd.type == ParsedCommand.Type.DELETE) {
                    taskManager.deleteTask(taskIndex);
                    ui.sendMessage(String.format("Removing tasks on your list doesn't make it go away. Removed:\n    %s", t));
                    persist();
                    return;
                }
                break;

            case TODO:
                if (cmd.arg1 == null || cmd.arg1.trim().isEmpty()) throw new EmptyTaskException("todo");
                ToDo newToDo = taskManager.addToDo(cmd.arg1.trim());
                ui.sendMessage(String.format("Added this task:\n  %s\n%s", newToDo, listStatus()));
                persist();
                return;

            case DEADLINE:
                if (cmd.arg1 == null || cmd.arg1.trim().isEmpty()) throw new EmptyTaskException("deadline");
                if (cmd.deadline == null) throw new EmptyDeadlineTime();
                Deadline newDeadline = taskManager.addDeadline(cmd.arg1.trim(), cmd.deadline);
                ui.sendMessage(String.format("Time is tickin'!\n  %s\n%s", newDeadline, listStatus()));
                persist();
                return;

            case EVENT:
                if (cmd.arg1 == null || cmd.arg1.trim().isEmpty()) throw new EmptyTaskException("event");
                if (cmd.arg2 == null || cmd.arg2.trim().isEmpty()) throw new EmptyEventTime();
                if (cmd.arg3 == null || cmd.arg3.trim().isEmpty()) throw new EmptyEventTime();
                Events newEvent = taskManager.addEvents(cmd.arg1.trim(), cmd.arg2.trim(), cmd.arg3.trim());
                ui.sendMessage(String.format("Eventing!\n   %s\n%s", newEvent, listStatus()));
                persist();
                return;

            default:
                throw new InvalidCommandException(cmd.type.toString());
        }
    }


    private void sayGoodbye() {
        ui.sayGoodbye();
    }

    private void persist() {
        try {
            storage.save(new ArrayList<>(taskManager.getTasks()));
        } catch (IOException e) {
            ui.sendMessage("Failed to save tasks: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        BingyBot bot = new BingyBot();
        bot.run();
    }
}