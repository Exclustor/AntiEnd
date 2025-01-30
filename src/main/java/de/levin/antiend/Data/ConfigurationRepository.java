package de.levin.antiend.Data;

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
    boolean sendMessageInActionbar;
    List<String> entryDisabledInWorlds = new ArrayList<>();

    @Data
    public static class Bypass {
        String permission;
        boolean allowBypass;
    }
}
