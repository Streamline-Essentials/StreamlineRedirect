package host.plas.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import host.plas.StreamlineRedirect;
import lombok.Getter;
import lombok.Setter;
import singularity.data.console.CosmicSender;

import java.time.Duration;

public class PlayerManager {
    @Getter @Setter
    private static Cache<CosmicSender, Integer> tickedPlayers = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(10))
            .build();

    public static void addPlayer(CosmicSender player) {
        if (hasPlayer(player)) return;

        tickedPlayers.put(player, 0);
    }

    public static boolean hasPlayer(CosmicSender player) {
        return tickedPlayers.asMap().containsKey(player);
    }

    public static void removePlayer(CosmicSender player) {
        tickedPlayers.invalidate(player);
    }

    public static void tickPlayer(CosmicSender player) {
        if (! hasPlayer(player)) addPlayer(player);

        int ticks = getTicks(player);
        ticks ++;

        tickedPlayers.put(player, ticks);
    }

    public static boolean tickPlayer(CosmicSender player, boolean kick) {
        tickPlayer(player);

        if (checkNeedsKick(player)) {
            return kick;
        }

        return false;
    }

    public static int getTicks(CosmicSender player) {
        if (! hasPlayer(player)) return -1;

        return tickedPlayers.getIfPresent(player);
    }

    public static boolean checkNeedsKick(CosmicSender player) {
        return getTicks(player) >= StreamlineRedirect.getMainConfig().getMaxRedirects();
    }
}
