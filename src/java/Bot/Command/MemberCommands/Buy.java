package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Shop.IShopItem;
import Bot.Database.IDataBaseManager;
import Bot.Shop.ShopItemManager;

import java.util.List;

public class Buy implements ICommand {
   private final ShopItemManager shopItemManager;

   public Buy(ShopItemManager shopItemManager) {
      this.shopItemManager = shopItemManager;
   }

   @Override
   public void execute(CommandContext commandContext) {
      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      final List<String> args = commandContext.getArgs();

      if (args.isEmpty() || args.size() < 2) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      IShopItem item = shopItemManager.getShopItem(args.get(0));

      if (item == null) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve an item with this name").queue();
         return;
      }

      int itemAmount;

      try {
         itemAmount = Integer.parseInt(args.get(1));
      } catch (NumberFormatException error) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of items").queue();
         return;
      }

      if (itemAmount > 10) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You can purchase max. 10 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getBalance(commandContext.getAuthorID()) - (item.getPrice() * itemAmount) < 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setBalance(commandContext.getAuthorID(), -item.getPrice() * itemAmount);
      IDataBaseManager.INSTANCE.setInventory(commandContext.getAuthorID(), item.getCategory() , item.getName(), itemAmount);

      commandContext.getChannel().sendMessage(":moneybag: Congratulations, you successfully bought **" + itemAmount + " " + item.getName() + "** for **" + (item.getPrice() * itemAmount) + "** fluffies").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "buy [itemName] [1-10]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Buys the specified amount of a item from the shop";
   }

   @Override
   public String getName() {
      return "buy";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }
}
