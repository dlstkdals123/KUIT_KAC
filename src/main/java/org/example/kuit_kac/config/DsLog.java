import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DsLog {

    private final DataSource ds;

    @EventListener(ApplicationReadyEvent.class)
    public void log() {
        if (ds instanceof HikariDataSource h) {
            System.out.println("[DS] url=" + h.getJdbcUrl() + ", user=" + h.getUsername());
        } else {
            System.out.println("[DS] ds=" + ds.getClass().getName());
        }
    }
}