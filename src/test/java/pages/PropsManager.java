package pages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsManager {
    private static String testUserLogin;
    private static String testUserPass;

    private PropsManager() {
    }

    public static String testUserLogin() {
        return testUserLogin;
    }

    public static String testUserPass() {
        return testUserPass;
    }

    static {
        try (InputStream inputStream = PropsManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(inputStream);
            testUserLogin = props.getProperty("testuser.login");
            testUserPass = props.getProperty("testuser.password");
        } catch (IOException e) {
            //TODO подумать над норм хендлингом
            e.printStackTrace();
        }
    }
}
