import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import util.Parser;
import util.Parser.ParsedCommand;



public class Storage {
    private final File file;

    /**
     * Create a new instance that takes in a file name and reads and writes from the data directory.
     *
     * @param fileName name of the file used for read and write
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
     * Saves all tasks into file
     *
     * @param list the list of tasks written into file
     * @throws IOException upon IO error
     */
    public void save(ArrayList<Task> list) throws IOException {
        FileWriter fw = new FileWriter(file);
        try {
            for (Task t : list) {
                fw.write(t.toString());
                fw.write(System.lineSeparator());
            }
        } finally {
            fw.close();
        }
    }

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
                Task task = null;
                switch (cmd.type) {
                    case TODO:
                        task = new ToDo(cmd.arg1);
                        break;
                    case DEADLINE:
                        task = new Deadline(cmd.arg1, cmd.arg2);
                        break;
                    case EVENT:
                        task = new Events(cmd.arg1, cmd.arg2, cmd.arg3);
                        break;
                    default:
                        continue; // skip unknown/malformed lines
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
