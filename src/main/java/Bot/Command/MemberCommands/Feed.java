package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Feed implements ICommand {
   private final ShopItemManager shopItemManager;

   public Feed(ShopItemManager shopItemManager) {
      this.shopItemManager = shopItemManager;
   }

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
      final List<String> args = commandContext.getArgs();
      long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(memberID, "sleep") - System.currentTimeMillis();
      int itemAmount;

      if (sleepCooldown > 0) {
         channel.sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (int)(((sleepCooldown / 1000) / 60) % 60) + "** minutes").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setCooldown(memberID, "sleep", 0);

      if (args.isEmpty() || args.size() < 2) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      IShopItem item = shopItemManager.getShopItem(args.get(0));

      if (item == null) {
         channel.sendMessage("<:RedCross:782229279312314368> Could not resolve this item").queue();
         return;
      }

      try {
         itemAmount = Integer.parseInt(args.get(1));

      } catch (NumberFormatException error) {
         channel.sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of item").queue();
         return;
      }

      if (itemAmount > 5) {
         channel.sendMessage("<:RedCross:782229279312314368> You can only feed max. 5 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(memberID, item.getName()) - itemAmount < 0) {
         channel.sendMessage("<:RedCross:782229279312314368> You do not own that many items").queue();
         return;
      }

      int oldValue = IDataBaseManager.INSTANCE.getAlpacaValues(memberID, item.getCategory());

      if (oldValue + (item.getSaturation() * itemAmount) > 100) {
         channel.sendMessage("<:RedCross:782229279312314368> You would overfeed your alpaca").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setInventory(memberID, item.getName(), -itemAmount);
      IDataBaseManager.INSTANCE.setAlpacaValues(memberID, item.getCategory(), (item.getSaturation() * itemAmount));

      if (item.getName().equals("salad")) {
         channel.sendMessage("\uD83E\uDD57 Your alpaca eats the " + (itemAmount == 1 ? itemAmount + " green salad" : itemAmount + " green salads") + " in one bite and is happy **Hunger + " + (item.getSaturation() * itemAmount) + "**").queue();

      } else {
         channel.sendMessage("\uD83D\uDCA7 Your alpaca eagerly drinks the " + (itemAmount == 1 ? itemAmount + " waterbottle" : itemAmount + " waterbottles") + " empty **Thirst + " + (item.getSaturation() * itemAmount) + "**").queue();
      }
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "feed [itemname] [1-5]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Feeds your alpaca with the specified item";
   }

   @Override
   public String getName() {
      return "feed";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }
}