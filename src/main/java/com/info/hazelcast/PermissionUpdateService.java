package com.info.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.config.PermissionConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.logging.Logger;

public class PermissionUpdateService implements Closeable {

    private static final Logger LOGGER = Logger.getLogger(PermissionUpdateService.class.getSimpleName());

    private Config config;
    private HazelcastInstance liteMember;

    public PermissionUpdateService(String configFileLocation) throws FileNotFoundException {
        config = new FileSystemXmlConfig(configFileLocation);
        config.setLiteMember(true);
        liteMember = Hazelcast.newHazelcastInstance(config);
    }

    public String getClientPermissions() {
        return liteMember.getConfig().getSecurityConfig().getClientPermissionConfigs().toString();
    }

    public void updateClientPermissions() {
        LOGGER.info("Updating client permissions");
        Set<PermissionConfig> newClientPermissionConfigs = config.getSecurityConfig().getClientPermissionConfigs();
        liteMember.getConfig().getSecurityConfig().setClientPermissionConfigs(newClientPermissionConfigs);
    }

    public void close() {
        if (liteMember != null) {
            liteMember.shutdown();
        }
    }
}
