package host.plas.events;

import host.plas.StreamlineRedirect;
import host.plas.managers.PlayerManager;
import singularity.data.console.CosmicSender;
import singularity.events.server.KickedFromServerEvent;
import tv.quaint.events.BaseEventHandler;
import tv.quaint.events.BaseEventListener;
import tv.quaint.events.processing.BaseProcessor;

public class MainListener implements BaseEventListener {
    public MainListener() {
        StreamlineRedirect.getInstance().logInfo("MainListener loaded!");
        BaseEventHandler.bake(this, StreamlineRedirect.getInstance());
    }

    @BaseProcessor
    public void onKick(KickedFromServerEvent event) {
        CosmicSender player = event.getSender();
        if (player == null) return;

        String fromServer = event.getFromServer();

        if (! fromServer.equalsIgnoreCase("none")) {
            StreamlineRedirect.getMainConfig().getRedirects().forEach(configuredRedirect -> {
                if (configuredRedirect.getFromServers().contains(fromServer)) {
                    if (! configuredRedirect.getToServers().isEmpty()) {
                        if (PlayerManager.tickPlayer(player, true)) {
                            event.setCancelled(true);
                        } else {
                            String toServer = configuredRedirect.getToServers().get(0);
                            event.setToServer(toServer); // TODO: Add a way to check if a server is online.
                        }
                    } else {
                        PlayerManager.removePlayer(player);
                    }
                }
            });
        } else {
            PlayerManager.removePlayer(player);
        }
    }
}
