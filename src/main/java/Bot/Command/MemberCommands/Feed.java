package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Feed implements ICommand {
   private final ShopItemManager shopItemManager;

   public Feed(ShopItemManager shopItemManager) {
      this.shopItemManager = shopItemManager;
   }

   @Override
   public void execute(CommandContext commandContext) {

      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(commandContext.getAuthorID(), "sleep") - System.currentTimeMillis();
      int remainingMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown);

      if (sleepCooldown > 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (remainingMinutes == 1 ? remainingMinutes + "** minute" : remainingMinutes + "** minutes")).queue();
         return;
      }

      final List<String> args = commandContext.getArgs();

      if (args.isEmpty() || args.size() < 2) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      IShopItem item = shopItemManager.getShopItem(args.get(0));

      if (item == null) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve this item").queue();
         return;
      }

      int itemAmount;

      try {
         itemAmount = Integer.parseInt(args.get(1));

      } catch (NumberFormatException error) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of item").queue();
         return;
      }

      if (itemAmount > 5) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You can only feed max. 5 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(commandContext.getAuthorID(), item.getName()) - itemAmount < 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own that many items").queue();
         return;
      }

      int oldValue = IDataBaseManager.INSTANCE.getAlpacaValues(commandContext.getAuthorID(), item.getCategory());

      if (oldValue + (item.getSaturation() * itemAmount) > 100) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You would overfeed your alpaca").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setInventory(commandContext.getAuthorID(), item.getName(), -itemAmount);
      IDataBaseManager.INSTANCE.setAlpacaValues(commandContext.getAuthorID(), item.getCategory(), (item.getSaturation() * itemAmount));

      if (item.getName().equals("salad")) {
         commandContext.getChannel().sendMessage("\uD83E\uDD57 Your alpaca eats the " + (itemAmount == 1 ? itemAmount + " green salad" : itemAmount + " green salads") + " in one bite and is happy **Hunger + " + (item.getSaturation() * itemAmount) + "**").queue();

      } else {
         commandContext.getChannel().sendMessage("\uD83D\uDCA7 Your alpaca eagerly drinks the " + (itemAmount == 1 ? itemAmount + " waterbottle" : itemAmount + " waterbottles") + " empty **Thirst + " + (item.getSaturation() * itemAmount) + "**").queue();
      }
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "feed [itemName] [1-5]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Feeds your alpaca with the specified item";
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