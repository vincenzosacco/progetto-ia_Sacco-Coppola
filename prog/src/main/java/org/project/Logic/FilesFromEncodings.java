package org.project.Logic;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.embAsp.Group;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Utility class to get files from encodings directory.
 */
public class FilesFromEncodings {
    public static final ArrayList<String> STRATEGIES = new ArrayList<>();

    // This block of code is executed when the class is loaded (first time it is referenced in the code)
    static {
        Path dir = Paths.get(Settings.PATH_ENCOD);
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
