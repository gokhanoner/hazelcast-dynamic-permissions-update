package com.info.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.security.AccessControlException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ClientPermissionsTest {

    private static final String MAP_1 = "map-1";
    private static final String MAP_2 = "map-2";
    private static final String NEW_CONFIG_LOCATION = "src/test/resources/hazelcast-updated.xml";

    private static final String PERMISSION_DENIED_MAP_2 = "Permission (\"com.hazelcast.security.permission.MapPermission\" \"map-2\" \"create\") denied!";
    private static final String MAP_1_PERMISSION = "PermissionConfig{type=MAP, name='map-1', principal='*', endpoints=[127.0.0.1], actions=[read, create, put, remove]}";
    private static final String MAP_2_PERMISSION = "PermissionConfig{type=MAP, name='map-2', principal='*', endpoints=[127.0.0.1], actions=[read, create, put, remove]}";


    @Before
    public void init() {
        Hazelcast.newHazelcastInstance();
    }

    @After
    public void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Test
    public void successfulClientPermissions() {
        try {
            HazelcastInstance client = HazelcastClient.newHazelcastClient();
            client.getMap(MAP_1);
            //If no exception thrown then it's successful
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    @Test
    public void failedClientPermissions() {
        try {
            HazelcastInstance client = HazelcastClient.newHazelcastClient();
            client.getMap(MAP_2);
            fail("Expected an AccessControlException to be thrown");
        } catch (AccessControlException e) {
            assertThat(e.getMessage(), is(PERMISSION_DENIED_MAP_2));
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    @Test
    public void successfulClientPermissionUpdate() throws FileNotFoundException {
        //Apply permissions & check applied
        try (PermissionUpdateService permissionUpdateService = new PermissionUpdateService(NEW_CONFIG_LOCATION)) {
            assertThat(permissionUpdateService.getClientPermissions(), containsString(MAP_1_PERMISSION));
            assertThat(permissionUpdateService.getClientPermissions(), not(containsString(MAP_2_PERMISSION)));
            permissionUpdateService.updateClientPermissions();
            assertThat(permissionUpdateService.getClientPermissions(), containsString(MAP_1_PERMISSION));
            assertThat(permissionUpdateService.getClientPermissions(), containsString(MAP_2_PERMISSION));
        }
        //Connect with a client & confirm
        try {
            HazelcastInstance client = HazelcastClient.newHazelcastClient();
            client.getMap(MAP_2);
            //If no exception thrown then it's successful
        } finally {
            HazelcastClient.shutdownAll();
        }
    }
}
