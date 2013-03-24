package ch.jamiete.easymail.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ch.jamiete.easymail.EasyMail;

public abstract class MasterCommand implements CommandExecutor {
    protected EasyMail mail;

    public MasterCommand(final EasyMail mail) {
        this.mail = mail;
    }

    public abstract void execute(CommandSender sender, Player player, boolean isPlayer, String[] args);

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        this.execute(sender, sender instanceof Player ? (Player) sender : null, sender instanceof Player, args);
        return true;
    }

}
