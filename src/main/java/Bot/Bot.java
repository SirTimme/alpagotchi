package Bot;

import Bot.Database.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot {

    public Bot() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        new JDABuilder()
                .setToken(Config.get("TOKEN"))
                .addEventListeners(new CommandListener())
                .setActivity(Activity.playing("Alpacas <3"))
                .build();
    }

    public static void main(String[] args) throws LoginException, SQLException {
        new Bot();
    }
}
