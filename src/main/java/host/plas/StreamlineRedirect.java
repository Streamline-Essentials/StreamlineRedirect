package host.plas;

import lombok.Getter;
import lombok.Setter;
import net.streamline.api.modules.SimpleModule;
import net.streamline.thebase.lib.pf4j.PluginWrapper;
import host.plas.configs.MainConfig;
import host.plas.events.MainListener;

public class StreamlineRedirect extends SimpleModule {
    @Getter @Setter
    private static StreamlineRedirect instance;
    @Getter @Setter
    private static MainConfig mainConfig;
    @Getter @Setter
    private static MainListener mainListener;

    public StreamlineRedirect(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void onEnable() {
        instance = this;

        mainConfig = new MainConfig();

        mainListener = new MainListener();
    }
}
