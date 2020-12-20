package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Shop.IShopItem;
import Bot.Database.IDataBaseManager;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Buy implements ICommand {
   private final ShopItemManager shopItemManager;

   public Buy(ShopItemManager shopItemManager) {
      this.shopItemManager = shopItemManager;
   }

   @Override
   public void handle(CommandContext commandContext) {
      final List<String> args = commandContext.getArgs();
      final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
      final TextChannel channel = commandContext.getChannel();
      int itemAmount;

      if (args.isEmpty()) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      IShopItem item = shopItemManager.getShopItem(args.get(0));

      if (item == null) {
         channel.sendMessage("<:RedCross:782229279312314368> No item with this name found").queue();
         return;
      }

      try {
         itemAmount = Integer.parseInt(args.get(1));

      } catch (NumberFormatException | IndexOutOfBoundsException error) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve the amount of items").queue();
         return;
      }

      if (itemAmount > 10) {
         channel.sendMessage("<:RedCross:782229279312314368> You can purchase max. 10 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(memberID, "currency") - (item.getPrice() * itemAmount) < 0) {
         channel.sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setInventory(memberID, "currency", -(item.getPrice() * itemAmount));
      IDataBaseManager.INSTANCE.setInventory(memberID, item.getName(), itemAmount);

      channel.sendMessage(":moneybag: Congratulations, you successfully bought **" + itemAmount + " " + item.getName() + "** for **" + (item.getPrice() * itemAmount) + "** fluffies").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "buy [itemname] [1-10]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Buys the specified amount of a item from the shop";
   }

   @Override
   public String getName() {
      return "buy";
   }

   @Override
   public String getPermissionLevel() {
      return "member";
   }
}
