package org.example.kuit_kac.global.util.dev;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "debug.whitelist")
public class DevWhitelistProperties {
    private boolean enabled = true;
    private List<String> kids = new ArrayList<>();

    public boolean allows(String kid) {
        if (kid == null || kid.isBlank()) return false;
        if (!enabled) return true;
        return kids.stream().anyMatch(k -> k != null && k.equals(kid));
    }
}
