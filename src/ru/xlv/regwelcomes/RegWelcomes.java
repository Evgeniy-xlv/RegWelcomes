package ru.xlv.regwelcomes;

import com.xxmicloxx.NoteBlockAPI.Song;
import fr.skytasul.music.MusicInventory;
import fr.skytasul.music.MusicMain;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.xlv.regwelcomes.network.PacketUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Xlv on 06.09.2017.
 */
public class RegWelcomes extends JavaPlugin {

    public static HashMap<String, String> playerData = new HashMap<>();
    public static HashMap<String, RegionData> regData = new HashMap<>();

    public static YamlConfiguration config;

    public static String PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "RegWelcomes" + ChatColor.GRAY + "] ";
    public static String regionRef = "<RW_REGION>";
    public static String playerRef = "<RW_PLAYER>";

    @Override
    public void onEnable() {
        super.onEnable();
        loadConfig();
        loadRegData();
        getCommand("regwelcomes").setExecutor(new CommandManager());
        getServer().getPluginManager().registerEvents(new RegWelcomesEvents(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    protected static void loadConfig(){
        YamlConfiguration config = new YamlConfiguration();
        String path = "plugins/RegWelcomes/config.yml";
        try {
            config.load(new File(path));
            if(config.contains("prefix"))
                PREFIX = config.getString("prefix");
        } catch (FileNotFoundException e) {
            try {
                config.set("prefix", PREFIX);
                config.save(new File(path));
                loadConfig();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected static void loadRegData(){
        config = new YamlConfiguration();
        String path = "plugins/RegWelcomes/regData.yml";
        try {
            config.load(new File(path));
            for(String key : config.getKeys(false)){
                RegionData reg = new RegionData(key);
                if(config.contains(key + ".welcomeTitle"))
                    reg.welcomeTitle = config.getString(key + ".welcomeTitle");
                if(config.contains(key + ".byeTitle"))
                    reg.byeTitle = config.getString(key + ".byeTitle");
                if(config.contains(key + ".welcomeMessage"))
                    reg.welcomeMessage = config.getString(key + ".welcomeMessage");
                if(config.contains(key + ".byeMessage"))
                    reg.byeMessage = config.getString(key + ".byeMessage");
                if(config.contains(key + ".welcomeSubtitle"))
                    reg.welcomeSubtitle = config.getString(key + ".welcomeSubtitle");
                if(config.contains(key + ".byeSubtitle"))
                    reg.byeSubtitle = config.getString(key + ".byeSubtitle");
                if(config.contains(key + ".sound"))
                    reg.soundName = config.getString(key + ".sound");
                if(config.contains(key + ".song"))
                    reg.songID = config.getInt(key + ".song");
                if(config.contains(key + ".fadeIn"))
                    reg.fadeIn = config.getInt(key + ".fadeIn");
                if(config.contains(key + ".fadeOut"))
                    reg.fadeOut = config.getInt(key + ".fadeOut");
                if(config.contains(key + ".stay"))
                    reg.stay = config.getInt(key + ".stay");
                regData.put(key, reg);
            }
        } catch (FileNotFoundException e) {
            try {
                config.save(new File(path));
                loadRegData();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String worldString(World world, String name) {
        return world.getName() + ":" + name;
    }

    public static String removeWorld(World world, String name) {
        return StringUtils.remove(name, world.getName() + ":");
    }

    public static void titleEntry(Player player, String i) {
        RegionData reg = regData.get(i);
        String stitle = "";
        String title = "";
        if(reg == null) return;
        if(reg.welcomeTitle != null){
            title = StringUtils.replace(StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                    reg.welcomeTitle), regionRef, i), playerRef, player.getName());
        }
        if(reg.welcomeSubtitle != null) {
            stitle = StringUtils.replace(StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                    reg.welcomeSubtitle), regionRef, i), playerRef, player.getName());
        }
        sendTitle(player, reg.fadeIn, reg.stay, reg.fadeOut, title, stitle);
    }

    public static void titleExit(Player p, String i) {
        RegionData reg = regData.get(i);
        String stitle = "";
        String title = "";
        if(reg == null) return;
        if(reg.byeTitle != null) {
            title = StringUtils.replace(StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                    reg.byeTitle), regionRef, i), playerRef, p.getName());
        }
        if(reg.byeSubtitle != null) {
            stitle = StringUtils.replace(StringUtils.replace(ChatColor.translateAlternateColorCodes('&',
                    reg.byeSubtitle), regionRef, i), playerRef, p.getName());
        }
        sendTitle(p, reg.fadeIn, reg.stay, reg.fadeOut, title, stitle);
    }

    public static void playSongFor(Player player, String region){
        RegionData reg = regData.get(region);
        if(reg == null) return;
        if(reg.songID != -1) {
            MusicInventory musicInventory = MusicInventory.getInv(player.getUniqueId());
            Song song = MusicMain.songs.get(reg.songID);
            musicInventory.play(player, song, true);
        }
    }

    public static void playSoundAt(Player player, String region){
        RegionData reg = regData.get(region);
        if(reg == null) return;
        if(reg.soundName != null){
            player.playSound(player.getLocation(), Sound.valueOf(reg.soundName), 1, 1);
        }
    }

    public static void sendWelcomeMessageTo(Player player, String region){
        RegionData reg = regData.get(region);
        if(reg == null) return;
        if(reg.welcomeMessage != null){
            String message = reg.welcomeMessage.replaceAll(regionRef, region).replaceAll(playerRef, player.getName());
            player.sendMessage(PREFIX + message);
        }
    }

    public static void sendByeMessageTo(Player player, String region){
        RegionData reg = regData.get(region);
        if(reg == null) return;
        if(reg.byeMessage != null){
            String message = reg.byeMessage.replaceAll(regionRef, region).replaceAll(playerRef, player.getName());
            player.sendMessage(PREFIX + message);
        }
    }

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PacketUtil.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
    }
}
