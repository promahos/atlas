import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestCounter {
    public static void main(String[] args) throws IOException {

        String testClassesDirectory = "target/test-classes";
        URL testClassesURL = Paths.get(testClassesDirectory).toUri().toURL();

        List<URL> testClassesURLs = new ArrayList<>();
        testClassesURLs.add(testClassesURL);

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{testClassesURL}, ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(testClassesURLs)
                .addClassLoaders(classLoader)
                .setScanners(
                        Scanners.SubTypes,
                        Scanners.TypesAnnotated,
                        Scanners.MethodsAnnotated));

        Set<Method> testMethods = reflections.getMethodsAnnotatedWith(Test.class);

        String methodsNames = testMethods
                .stream()
                .map(Method::getName)
                .collect(Collectors.joining(", "));

        Path targetFilePath = Paths.get("results", "testMethodsNamesAll.txt");
        Files.write(targetFilePath, methodsNames.getBytes(StandardCharsets.UTF_8));
    }
}
