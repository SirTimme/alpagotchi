package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.User;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.time.Instant;

public class Init implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);

        if (user != null) {
            event.reply(Emote.REDCROSS + " You already own an alpaca").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("User information")
             .setDescription("Im glad, that Alpagotchi interests you.\nHere are two important points before you can start:")
             .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
             .addField(
                     "__§1 Storage of the UserID__",
                     "Alpagotchi stores your personal Discord UserID in order to work, but this is public information and can be accessed by everyone.",
                     false
             )
             .addField(
                     "__§2 Deletion of the UserID__",
                     "If you change your mind about storing your UserID,\nuse the `/delete` command to delete your data at any time.",
                     false
             )
             .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
             .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build())
             .addActionRow(
                     Button.success("acceptInit", "Accept"),
                     Button.danger("declineInit", "Decline")
             )
             .setEphemeral(true)
             .queue();
    }
}
