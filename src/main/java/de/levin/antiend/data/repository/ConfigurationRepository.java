package de.levin.antiend.data.repository;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
public class ConfigurationRepository {
    private Locale language;
    private boolean disableEnd;
    private Bypass bypass = new Bypass();
    private boolean sendPreventMessage;
    private boolean sendMessagesAsActionbar;
    private boolean disableJumpIntoPortal;
    private boolean disableEnderEyeThrow;
    private List<String> entryDisabledInWorlds = new ArrayList<>();
    private List<String> sendCommandsOnPortalEnter = new ArrayList<>();
    private Hologram hologram;

    @Data
    public static class Bypass {
        private String permission;
        private boolean allowBypass;
    }

    @Data
    public static class Hologram {
        private double spaceBetweenEachLine;
        private Schedule schedule;
        private DynamicOpening dynamicOpening;
    }

    @Data
    public static class Schedule {
        private boolean enabled;
        private String monday;
        private String tuesday;
        private String wednesday;
        private String thursday;
        private String friday;
        private String saturday;
        private String sunday;
    }

    @Data
    public static class DynamicOpening {
        private boolean enabled;
        private short interval;
        private short duration;
    }
}
