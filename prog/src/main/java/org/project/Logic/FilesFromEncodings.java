package org.project.Logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * Utility class to get files from encodings directory.
 */
public class FilesFromEncodings {
    public static final Vector<String> STRATEGIES = new Vector<>();

    // This block of code is executed when the class is loaded (first time it is referenced in the code)
    static {
        Path dir = Paths.get(LogicSettings.PATH_ENCOD);
        // Get directory names
        try (Stream<Path> paths = Files.list(dir)) {
            paths.forEachOrdered(enc ->{
               STRATEGIES.add(enc.getFileName().toString());
            });

        } catch (
        IOException e) {
            throw new RuntimeException(e);
        }
    }

}
