package com.info.hazelcast;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class UpdatePermissions {

    private static final Logger LOGGER = Logger.getLogger(UpdatePermissions.class.getSimpleName());

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: dynamic-security-update-1.0.jar [configFileLocation]");
        }

        try (PermissionUpdateService permissionUpdateService = new PermissionUpdateService(args[0])) {
            LOGGER.info("Current Permissions : " + permissionUpdateService.getClientPermissions());
            permissionUpdateService.updateClientPermissions();
            LOGGER.info("Updated Permissions : " + permissionUpdateService.getClientPermissions());
        }
    }
}
