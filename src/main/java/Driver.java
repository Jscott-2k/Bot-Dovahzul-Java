import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Driver {

    private Dotenv dotenv;
    private JDA jda;
    private static Driver driver;

    public static void main(String[] args) {
        driver = new Driver();
    }

    public Driver() {
        run();
    }

    private void run() {
        dotenv = Dotenv.load();
        try {
            jda = JDABuilder.createDefault(getEnvVar("DISCORD_BOT_TOKEN"))
                    .addEventListeners(new MessageListener())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public String getEnvVar(String envName) {
        return dotenv.get(envName);
    }

    public JDA getJDA() {
        return jda;
    }

    public static Driver getDriver() {
        return driver;
    }
}