package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;

import java.util.List;

public class Gift implements ICommand {
   private final ShopItemManager shopItemManager;

   public Gift(ShopItemManager shopItemManager) {
      this.shopItemManager = shopItemManager;
   }

   @Override
   public void execute(CommandContext commandContext) {

      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      final List<String> args = commandContext.getArgs();

      if (args.isEmpty() || args.size() < 3) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (commandContext.getMessage().getMentionedUsers().isEmpty()) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the mentioned user").queue();
         return;
      }

      final long giftedUserID = commandContext.getMessage().getMentionedUsers().get(0).getIdLong();

      if (giftedUserID == commandContext.getAuthorID()) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You can not gift yourself items").queue();
         return;
      }

      if (!IDataBaseManager.INSTANCE.isUserInDB(giftedUserID)) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> The mentioned user does not own a alpaca, he have to use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      IShopItem giftedItem = shopItemManager.getShopItem(args.get(1));

      if (giftedItem == null) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the specified item").queue();
         return;
      }

      int giftedItemAmount;

      try {
         giftedItemAmount = Integer.parseInt(args.get(2));

      } catch (NumberFormatException error) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of items").queue();
         return;
      }

      if (giftedItemAmount > 5) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You can gift max. 5 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(commandContext.getAuthorID(), giftedItem.getName()) - giftedItemAmount < 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own that many items to gift").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setInventory(commandContext.getAuthorID(), giftedItem.getName(), -giftedItemAmount);
      IDataBaseManager.INSTANCE.setInventory(giftedUserID, giftedItem.getName(), giftedItemAmount);

      commandContext.getChannel().sendMessage("\uD83C\uDF81 You successfully gifted **" + giftedItemAmount + " " + giftedItem.getName() + "** to **" + commandContext.getJDA().getUserById(giftedUserID).getName() + "**").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "gift [@user] [itemName] [1-5]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Gifts the mentioned user the specified amount of items";
   }

   @Override
   public String getName() {
      return "gift";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }
}
