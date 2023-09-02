package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.db.IDatabase;
import bot.models.cooldown.CooldownUtils;
import bot.models.user.User;
import bot.commands.types.CommandType;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Work extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // is the alpaca currently sleeping?
        final long sleep = CooldownUtils.toMinutes(user.getCooldown().getSleep());
        if (sleep > 0) {
            event.reply(LocalizedResponse.get("work.error.currentlySleeping", locale, sleep)).setEphemeral(true).queue();
            return;
        }

        // did the alpaca already work?
        final long work = CooldownUtils.toMinutes(user.getCooldown().getWork());
        if (work > 0) {
            event.reply(LocalizedResponse.get("work.error.alreadyWorked", locale, work)).setEphemeral(true).queue();
            return;
        }

        // has the alpaca enough energy?
        final int energy = user.getAlpaca().getEnergy();
        if (energy < 10) {
            event.reply(LocalizedResponse.get("work.error.tooTired", locale)).setEphemeral(true).queue();
            return;
        }

        // is the alpaca happy enough?
        final int joy = user.getAlpaca().getJoy();
        if (joy < 15) {
            event.reply(LocalizedResponse.get("work.error.tooSad", locale)).setEphemeral(true).queue();
            return;
        }

        final var fluffies = (int) (Math.random() * 15 + 1);
        final var energyCost = (int) (Math.random() * 8 + 1);
        final var joyCost = (int) (Math.random() * 10 + 2);

        // update data
        user.getInventory().setCurrency(user.getInventory().getCurrency() + fluffies);
        user.getAlpaca().setEnergy(user.getAlpaca().getEnergy() - energyCost);
        user.getAlpaca().setJoy(user.getAlpaca().getJoy() - joyCost);
        user.getCooldown().setWork(CooldownUtils.setCooldown(20));
        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply(LocalizedResponse.get("work.successful", locale, fluffies, energyCost, joyCost)).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("work", "Let your alpaca work for fluffies")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Lässt dein Alpaka für Fluffies arbeiten");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}