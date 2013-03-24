package ch.jamiete.easymail.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ch.jamiete.easymail.EasyMail;
import ch.jamiete.easymail.EasyMailAPI;

public class GetMailCommand extends MasterCommand {

    public GetMailCommand(final EasyMail mail) {
        super(mail);
    }

    @Override
    public void execute(final CommandSender sender, final Player player, final boolean isPlayer, final String[] args) {
        if (!isPlayer) {
            sender.sendMessage(ChatColor.RED + "You can't use that command in the console.");
            return;
        }

        final EasyMailAPI api = EasyMailAPI.getInstance();
        final String name = player.getName();
        final int highest = api.getHighestMail(name);

        if (highest > -1) {
            final Inventory mails = this.mail.getServer().createInventory(null, 54, "Mail for " + name);
            for (int i = 0; i < highest + 1; i++) {
                final ItemStack mail = api.getMail(name, i);
                final ItemMeta meta = mail.getItemMeta();
                final List<String> lore = new ArrayList<String>(meta.getLore());
                lore.add("#" + i);
                meta.setLore(lore);
                mail.setItemMeta(meta);
                mails.addItem(mail);
            }
            player.openInventory(mails);
        } else {
            sender.sendMessage(ChatColor.RED + "You have no new mail.");
        }
    }
}
