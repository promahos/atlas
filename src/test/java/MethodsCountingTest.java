import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodsCountingTest {

    @Test
    public void verifyTestMethodsCounting() {

        Path targetFilePath = Paths.get("results", "testMethodsNamesAsTest.txt");
        try {
            Files.deleteIfExists(targetFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + targetFilePath, e);
        }

        var methods = BookingTest.class.getMethods();
        List<Method> methodsTest = new ArrayList<>();
        for (Method method : methods) {
            if (method.getAnnotation(Test.class) != null) {
                methodsTest.add(method);
            }
        }

        String methodsNames = methodsTest
                .stream()
                .map(Method::getName)
                .collect(Collectors.joining(", "));

        try {
            Files.write(targetFilePath, methodsNames.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file " + targetFilePath, e);
        }

        assertThat(targetFilePath)
                .exists();
    }
}
