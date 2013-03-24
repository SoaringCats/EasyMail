package ch.jamiete.easymail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class EasyMailAPI {
    private static EasyMailAPI INSTANCE;

    public static EasyMailAPI getInstance() {
        return EasyMailAPI.INSTANCE;
    }

    private final EasyMail mail;

    public EasyMailAPI(final EasyMail mail) {
        EasyMailAPI.INSTANCE = this;
        this.mail = mail;
    }

    public int addMail(final ItemStack book) {
        final BookMeta meta = (BookMeta) book.getItemMeta();

        if (book.getType() != Material.WRITTEN_BOOK || !meta.hasAuthor() || !meta.hasDisplayName() || !meta.hasPages()) {
            throw new RuntimeException("Invalid ItemStack supplied.");
        }

        if (meta.getPageCount() == 1) {
            return -1;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        final List<String> pages = new ArrayList<String>(meta.getPages());
        pages.remove(0);
        meta.setPages(pages);
        meta.setLore(Arrays.asList(ChatColor.GOLD + "Sent on", ChatColor.GOLD + " " + sdf.format(new Date(System.currentTimeMillis()))));
        book.setItemMeta(meta);

        final String name = meta.getDisplayName().split(" ")[2];

        final FileConfiguration conf = this.mail.getConfig();
        if (!conf.isConfigurationSection("mails." + name)) {
            conf.createSection("mails." + name);
        }

        final ConfigurationSection section = conf.getConfigurationSection("mails." + name);
        final int highest = this.getHighestMail(name);

        section.set(String.valueOf(highest + 1), book);

        this.mail.saveConfig();

        return highest + 1;
    }

    public int getHighestMail(final String name) {
        final FileConfiguration conf = this.mail.getConfig();
        if (!conf.isConfigurationSection("mails." + name)) {
            conf.createSection("mails." + name);
        }

        final ConfigurationSection section = conf.getConfigurationSection("mails." + name);
        int highest = 0;
        boolean found = false;
        for (final String key : section.getKeys(false)) {
            found = true;
            if (Integer.parseInt(key) > highest) {
                highest = Integer.parseInt(key);
            }
        }

        return found ? highest : -1;
    }

    public ItemStack getMail(final String name, final int id) {
        final FileConfiguration conf = this.mail.getConfig();
        if (!conf.isConfigurationSection("mails." + name)) {
            conf.createSection("mails." + name);
        }

        final ConfigurationSection section = conf.getConfigurationSection("mails." + name);
        for (final String key : section.getKeys(false)) {
            if (Integer.parseInt(key) == id) {
                return section.getItemStack(key);
            }
        }

        return null;
    }

    public String getOrdinal(final int nth) {
        final int tenRemainder = nth % 10;
        switch (tenRemainder) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public boolean isOk(final String characters, final String check) {
        final ArrayList<Character> allowed = new ArrayList<Character>();

        for (final char c : characters.toCharArray()) {
            allowed.add(c);
        }

        for (final char c : check.toCharArray()) {
            if (!allowed.contains(c)) {
                return false;
            }
        }

        return true;
    }

    public boolean isPlural(final int nth) {
        return nth != 1;
    }

    public boolean isUsername(final String check) {
        return this.isOk("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_", check);
    }

    public List<String> makeLore(final String... lore) {
        return lore == null ? null : Arrays.asList(lore);
    }

    public String makePage(final String... lines) {
        if (lines == null) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();

        for (final String line : lines) {
            builder.append(line);
            builder.append('\n');
        }

        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }

        return builder.toString();
    }

    public void sortMails(final String name) {
        final FileConfiguration conf = this.mail.getConfig();
        if (!conf.isConfigurationSection("mails." + name)) {
            conf.createSection("mails." + name);
        }

        final ConfigurationSection section = conf.getConfigurationSection("mails." + name);
        final List<ItemStack> things = new ArrayList<ItemStack>();

        for (final String key : section.getKeys(false)) {
            things.add(section.getItemStack(key));
            section.set(key, null);
        }

        for (int i = 0; i < things.size(); i++) {
            section.set(String.valueOf(i), things.get(i));
        }

        this.mail.saveConfig();
    }
}
