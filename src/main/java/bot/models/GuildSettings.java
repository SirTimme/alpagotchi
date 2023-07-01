package bot.models;

public class GuildSettings {
    private final long guildID;
    private String language;

    public GuildSettings(final long guildID) {
        this.guildID = guildID;
        this.language = "en";
    }

    public long getGuildID() {
        return this.guildID;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}