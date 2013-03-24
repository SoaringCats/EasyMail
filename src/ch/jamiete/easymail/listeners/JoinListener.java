package ch.jamiete.easymail.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ch.jamiete.easymail.EasyMail;
import ch.jamiete.easymail.EasyMailAPI;

public class JoinListener extends MasterListener {

    public JoinListener(final EasyMail mail) {
        super(mail);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final EasyMailAPI api = EasyMailAPI.getInstance();
        final String name = event.getPlayer().getName();

        api.sortMails(name);
        final int highest = api.getHighestMail(name) + 1;
        if (highest > -1) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "You have " + highest + " new " + (api.isPlural(highest) ? "mails" : "mail") + ". Use /mail to check.");
        }
    }

}
