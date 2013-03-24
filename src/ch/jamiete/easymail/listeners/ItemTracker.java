package ch.jamiete.easymail.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ch.jamiete.easymail.EasyMail;

public class ItemTracker extends MasterListener {

    public ItemTracker(final EasyMail mail) {
        super(mail);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        // TODO: Remove mail from storage if taken from mailbox.
        if (event.getInventory().getName().startsWith("Mail for")) {
            if (event.getInventory().getItem(event.getSlot()) == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        final ItemMeta meta = event.getItemDrop().getItemStack().getItemMeta();

        if (meta instanceof BookMeta) {
            if (meta.getDisplayName().startsWith("Note for")) {
                event.setCancelled(true);
            }
        }
    }

}
