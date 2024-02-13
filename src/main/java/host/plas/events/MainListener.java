package host.plas.events;

import net.streamline.api.events.server.KickedFromServerEvent;
import net.streamline.api.savables.users.StreamlinePlayer;
import host.plas.StreamlineRedirect;
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
        StreamlinePlayer player = event.getPlayer();
        if (player == null) return;

        String fromServer = event.getFromServer();

        if (! fromServer.equalsIgnoreCase("none")) {
            StreamlineRedirect.getMainConfig().getRedirects().forEach(configuredRedirect -> {
                if (configuredRedirect.getFromServers().contains(fromServer)) {
                    if (! configuredRedirect.getToServers().isEmpty()) {
                        String toServer = configuredRedirect.getToServers().get(0);
                        event.setToServer(toServer); // TODO: Add a way to check if a server is online.
                        StreamlineRedirect.getInstance().logDebug("Setting the toServer to " + toServer + " for player " + player.getName() + "!");
                    }
                }
            });
            StreamlineRedirect.getInstance().logDebug("Event's toServer is " + event.getToServer() + " for player " + player.getName() + "!");
        } else {
            StreamlineRedirect.getInstance().logDebug("Player " + player.getName() + " was kicked from the network!");
        }
    }
}
