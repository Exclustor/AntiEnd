package de.levin.antiend.data.repository;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
public class ConfigurationRepository {
    Locale language;
    boolean disableEnd;
    Bypass bypass = new Bypass();
    boolean sendPreventMessage;
    boolean sendMessagesAsActionbar;
    boolean disableJumpIntoPortal;
    boolean disableEnderEyeThrow;
    List<String> entryDisabledInWorlds = new ArrayList<>();
    List<String> sendCommandsOnPortalEnter = new ArrayList<>();
    Hologram hologram;


    @Data
    public static class Bypass {
        String permission;
        boolean allowBypass;
    }

    @Data
    public static class Hologram {
        double heightBetweenEachLine;
    }
}
