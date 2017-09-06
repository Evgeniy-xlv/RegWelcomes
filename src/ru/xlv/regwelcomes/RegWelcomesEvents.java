package ru.xlv.regwelcomes;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

import static ru.xlv.regwelcomes.RegWelcomes.*;

/**
 * Created by Xlv on 06.09.2017.
 */
public class RegWelcomesEvents implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        World world = to.getWorld();
        Map<String, ProtectedRegion> regions = WorldGuardPlugin.inst().getRegionManager(world).getRegions();
        Boolean outsider = true;

        for(String i : regions.keySet()) {
            if(regions.get(i).contains(to.getBlockX(), to.getBlockY(), to.getBlockZ())) {
                outsider = false;

                if(!playerData.containsKey(event.getPlayer().getName())) {
                    playerData.put(event.getPlayer().getName(), "null");
                }

                if(!playerData.get(event.getPlayer().getName()).equals(worldString(world, i))) {
                    playerData.put(event.getPlayer().getName(), worldString(world, i));
                    titleEntry(event.getPlayer(), i);
                    sendWelcomeMessageTo(event.getPlayer(), i);
                    playSongFor(event.getPlayer(), i);
                    playSoundAt(event.getPlayer(), i);
                }
            }
        }

        if(outsider && playerData.containsKey(event.getPlayer().getName())) {
            String string = removeWorld(world, playerData.get(event.getPlayer().getName()));
            titleExit(event.getPlayer(), string);
            sendByeMessageTo(event.getPlayer(), string);
            playerData.remove(event.getPlayer().getName());
        }
    }
}
