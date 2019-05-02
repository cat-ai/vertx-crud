package io.cat.ai.app.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.cat.ai.app.node.Http11ServerNode;
import io.cat.ai.app.node.WebSocketServerNode;

public class AppModule extends AbstractModule {

    @Override
    public void configure() {
        bind(Config.class).toProvider(ConfigProvider.class).asEagerSingleton();
        bind(Http11ServerNode.class).asEagerSingleton();
        bind(WebSocketServerNode.class).asEagerSingleton();
    }

    private static class ConfigProvider implements Provider<Config> {

        @Override
        public Config get() {
            return ConfigFactory.load("reference.conf");
        }
    }
}
