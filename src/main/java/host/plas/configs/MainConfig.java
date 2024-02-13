package host.plas.configs;

import host.plas.StreamlineRedirect;
import host.plas.configs.bits.ConfiguredRedirect;
import tv.quaint.storage.resources.flat.simple.SimpleConfiguration;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class MainConfig extends SimpleConfiguration {
    public MainConfig() {
        super("config.yml", StreamlineRedirect.getInstance().getDataFolder(), true);
    }

    @Override
    public void init() {

    }

    public ConcurrentSkipListSet<ConfiguredRedirect> getRedirects() {
        ConcurrentSkipListSet<ConfiguredRedirect> redirects = new ConcurrentSkipListSet<>();

        getResource().singleLayerKeySet("redirects").forEach(key -> {
            ConcurrentSkipListSet<String> fromServers = new ConcurrentSkipListSet<>(getResource().getStringList("redirects." + key + ".from"));
            List<String> toServer = getResource().getStringList("redirects." + key + ".to");

            ConfiguredRedirect redirect = new ConfiguredRedirect(key, fromServers, toServer);

            redirects.add(redirect);
        });

        return redirects;
    }
}
