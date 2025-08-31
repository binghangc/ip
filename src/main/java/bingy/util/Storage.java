package bingy.util;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import bingy.tasks.Deadline;
import bingy.tasks.Events;
import bingy.tasks.Task;
import bingy.tasks.ToDo;


/**
 * Handles persistence of tasks to and from the {@code data/} directory.
 * <p>
 * Ensures the data folder exists, and reads/writes a plain-text file that
 * represents tasks line-by-line. Deadlines are stored with ISO-8601 dates.
 */
public class Storage {
    private final File file;

    /**
     * Create a new instance that takes in a file name and reads and writes from the data directory.
     *
     * @param fileName the name of the file used for read and write.
     */
    public Storage(String fileName) {
        Path folder = Paths.get("data");
        try {
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create data directory", e);
        }
        Path filePath = folder.resolve(fileName);
        this.file = filePath.toFile();
    }

    /**
     * Writes all tasks to the storage file in a line-based format.
     * <p>
     * {@link bingy.tasks.Deadline} entries are serialized using their
     * {@code toStorageString()} (ISO-8601 date) while other tasks use
     * their {@code toString()} representation.
     *
     * @param list the tasks to persist; order is preserved.
     * @throws IOException if an I/O error occurs while writing.
     */
    public void save(ArrayList<Task> list) throws IOException {
        FileWriter fw = new FileWriter(file);
        try {
            for (Task t : list) {
                if (t instanceof Deadline) {
                    fw.write(((Deadline) t).toStorageString());
                } else {
                    fw.write(t.toString());

                }
                fw.write(System.lineSeparator());
            }
        } finally {
            fw.close();
        }
    }

    /**
     * Loads tasks from the storage file, if present.
     * <p>
     * Each line is parsed back into a concrete task using
     * {@link bingy.util.Parser#parseStorageLine(String)}. Missing files are treated as empty.
     *
     * @return a list containing all tasks found in the file (maybe empty).
     * @throws IOException if an I/O error occurs while reading.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            return tasks;
        }

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                Parser.ParsedCommand cmd = Parser.parseStorageLine(line);
                Task task;
                switch (cmd.type) {
                case TODO:
                    task = new ToDo(cmd.arg1);
                    break;
                case DEADLINE:
                    task = new Deadline(cmd.arg1, cmd.deadline);
                    break;
                case EVENT:
                    task = new Events(cmd.arg1, cmd.arg2, cmd.arg3);
                    break;
                default:
                    continue;
                }
                if (cmd.isDone) {
                    task.markDone();
                }
                tasks.add(task);
            }
        }

        return tasks;
    }


}
