package ch.jamiete.easymail.listeners;

import org.bukkit.event.Listener;
import ch.jamiete.easymail.EasyMail;

public abstract class MasterListener implements Listener {
    protected EasyMail mail;

    public MasterListener(final EasyMail mail) {
        this.mail = mail;
        this.mail.getServer().getPluginManager().registerEvents(this, this.mail);
    }

}
