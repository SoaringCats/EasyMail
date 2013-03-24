package ch.jamiete.easymail.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import ch.jamiete.easymail.EasyMail;
import ch.jamiete.easymail.EasyMailAPI;

public class SendMailCommand extends MasterCommand {

    public SendMailCommand(final EasyMail mail) {
        super(mail);
    }

    @Override
    public void execute(final CommandSender sender, final Player player, final boolean isPlayer, final String[] args) {
        if (!isPlayer) {
            sender.sendMessage(ChatColor.RED + "You can't use that command in the console.");
            return;
        }

        if (args.length == 0) {
            if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.WRITTEN_BOOK) {
                final BookMeta meta = (BookMeta) player.getItemInHand().getItemMeta();
                if (meta.hasAuthor() && meta.getDisplayName().startsWith(ChatColor.RESET + "Note for")) {
                    EasyMailAPI.getInstance().addMail(player.getItemInHand());
                    sender.sendMessage(ChatColor.GREEN + "Your mail has been sent! :)");
                    player.setItemInHand(null);
                } else {
                    sender.sendMessage(ChatColor.RED + "That's not a proper mail book.");
                    sender.sendMessage(meta.hasAuthor() + " " + meta.getDisplayName());
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Hold a written mail book in your hand or use " + ChatColor.ITALIC + "/send <username>");
                sender.sendMessage(player.getItemInHand().getType() + "");
            }

            return;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Uh-oh! You need to use " + ChatColor.ITALIC + "/send <username>" + ChatColor.RESET + ChatColor.RED + " to send mail!");
            return;
        }

        final String name = args[0];
        if (name.length() > 16 || name.length() == 0 || !EasyMailAPI.getInstance().isUsername(name)) {
            sender.sendMessage(ChatColor.RED + "Please enter a real player's name.");
            return;
        }

        final ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        final BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setDisplayName(ChatColor.RESET + "Note for " + name);
        meta.setLore(EasyMailAPI.getInstance().makeLore(ChatColor.GOLD + "Edit this book to send", ChatColor.GOLD + " mail to " + ChatColor.ITALIC + name + ".", ChatColor.GOLD + "Once you sign the book,", ChatColor.GOLD + " the mail will be sent."));
        meta.addPage(EasyMailAPI.getInstance().makePage(ChatColor.BOLD + " --[Easy Mail]--", "", ChatColor.LIGHT_PURPLE + "Enter your message on the next page. Any text on this page will be ignored.", "", ChatColor.GRAY + "When done, say /sendmail", "", ChatColor.BLACK + "This note will be sent to " + ChatColor.ITALIC + name));

        book.setItemMeta(meta);
        this.mail.books.add(book);

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "There is no space in your inventory to add the book. Please make space and run the command again.");
        } else {
            player.getInventory().addItem(book);
        }
    }
}
