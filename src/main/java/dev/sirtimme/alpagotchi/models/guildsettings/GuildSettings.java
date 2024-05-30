package dev.sirtimme.alpagotchi.models.guildsettings;

import dev.sirtimme.alpagotchi.localization.LocaleConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.Locale;

@Entity
@Table(name = "guild_settings", indexes = @Index(name = "idx_guild_id", unique = true, columnList = "guild_id"))
public class GuildSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NaturalId
    @Column(name = "guild_id", nullable = false)
    private long guildId;

    @Column(name = "locale", nullable = false)
    @Convert(converter = LocaleConverter.class)
    private Locale locale = Locale.ENGLISH;

    public GuildSettings() { }

    public GuildSettings(final long guildId, final Locale locale) {
        this.guildId = guildId;
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }
}