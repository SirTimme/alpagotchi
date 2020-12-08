package Bot.Database;

import Bot.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLiteDataSource implements IDataBaseManager {
    private final HikariDataSource dataSource;

    public SQLiteDataSource() {
        try {
            final File dbFile = new File("database.db");
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }

        } catch (IOException error) {
            error.printStackTrace();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtsCacheSize", "250");
        config.addDataSourceProperty("prepStmtsCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        try {
            Connection connection = dataSource.getConnection();
            final Statement statement = connection.createStatement();
            final String defaultPrefix = Config.get("PREFIX");

            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "guild_id VARCHAR(20) PRIMARY KEY NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix + "')");

            statement.execute("CREATE TABLE IF NOT EXISTS alpacas_manager (" +
                    "member_id VARCHAR(20) PRIMARY KEY NOT NULL, " +
                    "hunger INTEGER DEFAULT 100, " +
                    "thirst INTEGER DEFAULT 100, " +
                    "energy INTEGER DEFAULT 100)");

            statement.execute("CREATE TABLE IF NOT EXISTS inventory_manager (" +
                    "member_id VARCHAR(20) PRIMARY KEY NOT NULL, " +
                    "currency INTEGER DEFAULT 0, " +
                    "salad INTEGER DEFAULT 0, " +
                    "waterbottle INTEGER DEFAULT 0, " +
                    "battery INTEGER DEFAULT 0)");

            statement.execute("CREATE TABLE IF NOT EXISTS cooldown_manager (" +
                    "member_id VARCHAR(20) PRIMARY KEY NOT NULL, " +
                    "work VARCHAR(50) DEFAULT 0)");

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public String getPrefix(long guildID) {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?");
            preparedStatement.setString(1, String.valueOf(guildID));

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("prefix");
            }

            final PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)");
            insertStatement.setString(1, String.valueOf(guildID));
            insertStatement.execute();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return Config.get("PREFIX");
    }

    @Override
    public int getAlpaca(long memberID, String column) {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + column + " FROM alpacas_manager WHERE member_id = ?");
            preparedStatement.setString(1, String.valueOf(memberID));

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(column);
            }

            final PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO alpacas_manager(member_id, hunger, thirst, energy) VALUES(?, ?, ?, ?)");
            insertStatement.setString(1, String.valueOf(memberID));
            insertStatement.setInt(2, 100);
            insertStatement.setInt(3, 100);
            insertStatement.setInt(4, 100);
            insertStatement.execute();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return 100;
    }

    @Override
    public int getInventory(long memberID, String column) {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + column + " FROM inventory_manager WHERE member_id = ?");
            preparedStatement.setString(1, String.valueOf(memberID));

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(column);
            }

            final PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO inventory_manager(member_id, currency, salad, waterbottle, battery) VALUES(?, ?, ?, ?, ?)");
            insertStatement.setString(1, String.valueOf(memberID));
            insertStatement.setInt(2, 0);
            insertStatement.setInt(3, 0);
            insertStatement.setInt(4, 0);
            insertStatement.setInt(5, 0);
            insertStatement.execute();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return 0;
    }

    @Override
    public void decreaseValues() {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE alpacas_manager SET hunger = CASE WHEN (hunger > 0) THEN (hunger - 1) ELSE 0 END, " +
                    "thirst = CASE WHEN (thirst > 0) THEN (thirst - 1) ELSE 0 END," +
                    "energy = CASE WHEN (energy > 0) THEN (energy - 1) ELSE 0 END");
            preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public long getCooldown(long memberID, String column) {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + column + " FROM cooldown_manager WHERE member_id = ?");
            preparedStatement.setString(1, String.valueOf(memberID));

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(column);
            }

            final PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO cooldown_manager(member_id, work) VALUES(?, ?)");
            insertStatement.setString(1, String.valueOf(memberID));
            insertStatement.setLong(2, 0);
            insertStatement.execute();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setPrefix(long guildID, String newPrefix) {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?");
            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildID));
            preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void setAlpaca(long memberID, String column, int newValue) {
        int oldValue = IDataBaseManager.INSTANCE.getAlpaca(memberID, column);

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE alpacas_manager SET " + column + " = ? WHERE member_id = ?");
            preparedStatement.setInt(1, oldValue + newValue);
            preparedStatement.setString(2, String.valueOf(memberID));
            preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void setInventory(long memberID, String column, int newValue) {
        int oldValue = IDataBaseManager.INSTANCE.getInventory(memberID, column);

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE inventory_manager SET " + column + " = ? WHERE member_id = ?");
            preparedStatement.setInt(1, oldValue + newValue);
            preparedStatement.setString(2, String.valueOf(memberID));
            preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void setCooldown(long memberID, String column, long newValue) {

        try {
            Connection connection = dataSource.getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE cooldown_manager SET " + column + " = ? WHERE member_id = ?");
            preparedStatement.setLong(1, newValue);
            preparedStatement.setString(2, String.valueOf(memberID));
            preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
