package bot.commands.member;

import bot.commands.UserSlashCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Work extends UserSlashCommand {
    private final List<String> json;

    public Work() {
        this.json = List.of("hello", "world");
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // is the alpaca currently sleeping?
        final long sleep = TimeUnit.MILLISECONDS.toMinutes(user.getCooldown().getSleep() - System.currentTimeMillis());
        if (sleep > 0) {
            final var format = new MessageFormat(Responses.getLocalizedResponse("sleepCurrentlySleeping", locale));
            final var msg = format.format(new Object[]{ sleep });

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // did the alpaca already work?
        final long work = TimeUnit.MILLISECONDS.toMinutes(user.getCooldown().getWork() - System.currentTimeMillis());
        if (work > 0) {
            final var format = new MessageFormat(Responses.getLocalizedResponse("workAlreadyWorked", locale));
            final var msg = format.format(new Object[]{ work });
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // has the alpaca enough energy?
        final int energy = user.getAlpaca().getEnergy();
        if (energy < 10) {
            event.reply("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **/sleep**").setEphemeral(true).queue();
            return;
        }

        // is the alpaca happy enough?
        final int joy = user.getAlpaca().getJoy();
        if (joy < 15) {
            event.reply(":pensive: Your alpaca is too sad to work, give him some love with **/pet**").setEphemeral(true).queue();
            return;
        }

        final var message = getRandomMessage();
        final var fluffies = (int) (Math.random() * 15 + 1);
        final var energyCost = (int) (Math.random() * 8 + 1);
        final var joyCost = (int) (Math.random() * 10 + 2);

        // update db
        user.getInventory().setCurrency(user.getInventory().getCurrency() + fluffies);
        user.getAlpaca().setEnergy(user.getAlpaca().getEnergy() - energyCost);
        user.getAlpaca().setJoy(user.getAlpaca().getJoy() - joyCost);
        user.getCooldown().setWork(System.currentTimeMillis() + 1000L * 60 * 20);

        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply("⛏ " + message + " **Fluffies + " + fluffies + ", Energy - " + energyCost + ", Joy - " + joyCost + "**").queue();
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

    private String getRandomMessage() {
        return this.json.get((int) (Math.random() * this.json.size()));
    }
}