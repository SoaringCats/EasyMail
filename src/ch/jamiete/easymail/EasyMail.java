package ch.jamiete.easymail;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ch.jamiete.easymail.commands.GetMailCommand;
import ch.jamiete.easymail.commands.SendMailCommand;
import ch.jamiete.easymail.listeners.ItemTracker;
import ch.jamiete.easymail.listeners.JoinListener;

public class EasyMail extends JavaPlugin {
    public ArrayList<ItemStack> books = new ArrayList<ItemStack>();

    @Override
    public void onEnable() {
        if (!this.getConfig().contains("dont-edit-this-file")) {
            this.getConfig().set("dont-edit-this-file", "okay i won't i promise");
            this.saveConfig();
        }

        new EasyMailAPI(this);

        new ItemTracker(this);
        new JoinListener(this);
        this.getCommand("sendmail").setExecutor(new SendMailCommand(this));
        this.getCommand("getmail").setExecutor(new GetMailCommand(this));
    }

}
