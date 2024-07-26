package org.project.Logic;

import org.project.Logic.embAsp.Group;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * Utility class to get files from encodings directory.
 */
public class FilesFromEncodings {
    private static final String PATH_ENCOD = "prog/encodings";
    private static final String PACKAGE_NAME_EMBASP = "org.project.Logic.embAsp.";
    private static final String PATH_EMBASP = "prog/src/main/java/org/project/Logic/embAsp";
    private static final HashMap<String, Group> STRATEGY_TO_GROUP = new HashMap<>();


    // This block of code is executed when the class is loaded (first time it is referenced in the code)
    static {
        Path dir = Paths.get(PATH_ENCOD);

        try (Stream<Path> paths = Files.list(dir)) {
            paths.forEachOrdered(enc -> {
                String encName = enc.getFileName().toString();
                Group group = getGroupClass(PATH_EMBASP + "/" + encName, PACKAGE_NAME_EMBASP + encName);

                STRATEGY_TO_GROUP.put(enc.getFileName().toString(), group);
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private static Group toReturn = null;

    private static Group getGroupClass(String embAspPath, String embAspPackage) {
        Path dir = Paths.get(embAspPath);

        try (Stream<Path> paths = Files.list(dir)) {
            paths.forEach(path -> {
                //  IF THE FILE IS A JAVA FILE
                if (path.toString().endsWith(".java")) {
                    try {
                        URL[] urls = {path.toUri().toURL()};
                        URLClassLoader loader = new URLClassLoader(urls);
                        String className = path.getFileName().toString().replace(".java", "");
                        Class<?> clazz = Class.forName(embAspPackage + "." + className, true, loader);
                        if (Group.class.isAssignableFrom(clazz)) {
                            paths.close();
                            toReturn = (Group) clazz.getDeclaredConstructor().newInstance();
                        }

                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                             InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return toReturn;
    }

    public static Vector<String> getStrategies() {
        return new Vector<>(STRATEGY_TO_GROUP.keySet());
    }

    /**
     * Get the group mapped to the strategy.
     *
     * @param strategy the strategy
     * @return the group
     */
    public static Group getGroupFromStrategy(String strategy) {
        return STRATEGY_TO_GROUP.get(strategy);
    }
}
