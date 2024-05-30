package dev.sirtimme.alpagotchi.commands.member;

import dev.sirtimme.alpagotchi.commands.types.UserCommand;
import dev.sirtimme.alpagotchi.db.IDatabase;
import dev.sirtimme.alpagotchi.models.user.User;
import dev.sirtimme.alpagotchi.commands.types.CommandType;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Locale;

public class Outfit extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // selected outfit
        final var outfit = event.getOption("outfit").getAsString();

        // update data
        user.getAlpaca().setOutfit(outfit);
        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply(LocalizedResponse.get("outfit.successful", locale, outfit)).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var choices = List.of(
                new Command.Choice("default", "default"),
                new Command.Choice("gentleman", "gentleman"),
                new Command.Choice("lady", "lady")
        );

        final var option = new OptionData(OptionType.STRING, "outfit", "The new outfit", true)
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Das neue Outfit")
                .addChoices(choices);

        return Commands.slash("outfit", "Changes the outfit of your alpaca")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Gibt deinem Alpaka ein neues Outfit")
                       .addOptions(option);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}