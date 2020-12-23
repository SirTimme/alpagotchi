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
      int itemAmount;

      if (args.isEmpty() || args.size() < 2) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      IShopItem item = shopItemManager.getShopItem(args.get(0));

      if (item == null) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve this item").queue();
         return;
      }

      try {
         itemAmount = Integer.parseInt(args.get(1));

      } catch (NumberFormatException error) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve the amount of items").queue();
         return;
      }

      if (itemAmount > 5) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, you can only feed max. 5 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(memberID, item.getName()) - itemAmount < 0) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, you do not own that many items").queue();
         return;
      }

      int oldValue = IDataBaseManager.INSTANCE.getStatus(memberID, item.getCategory());

      if (oldValue + (item.getSaturation() * itemAmount) > 100) {
         channel.sendMessage("<:RedCross:782229279312314368> Invalid action, you would overfeed your alpaca").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setInventory(memberID, item.getName(), -itemAmount);
      IDataBaseManager.INSTANCE.setStatus(memberID, item.getCategory(), (item.getSaturation() * itemAmount));

      if (item.getName().equals("salad")) {
         channel.sendMessage("\uD83E\uDD57 Your alpaca eats the " + (itemAmount == 1 ? itemAmount + " green salad" : itemAmount + " green salads") + " in one bite and is happy **Hunger + " + (item.getSaturation() * itemAmount) + "**").queue();

      } else if (item.getName().equals("waterbottle")) {
         channel.sendMessage("\uD83D\uDCA7 Your alpaca eagerly drinks the " + (itemAmount == 1 ? itemAmount + " waterbottle" : itemAmount + " waterbottles") + " empty **Thirst + " + (item.getSaturation() * itemAmount) + "**").queue();

      } else {
         channel.sendMessage("\uD83D\uDD0B Your alpaca touches the " + (itemAmount == 1 ? itemAmount + " battery" : itemAmount + " batteries") + " and feels full of energy **Energy + " + (item.getSaturation() * itemAmount) + "**").queue();
      }
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "feed [itemName] [1-5]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Feeds your alpaca with the specified amount of a item";
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