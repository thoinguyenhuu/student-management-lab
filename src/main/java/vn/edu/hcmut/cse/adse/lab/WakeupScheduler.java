package vn.edu.hcmut.cse.adse.lab;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WakeupScheduler {
    private static final Logger logger = LoggerFactory.getLogger(WakeupScheduler.class);
    private static final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    @Scheduled(fixedRateString = "${wakeup.interval.ms:600000}")
    public void ping() {
        String targetUrl = resolveTargetUrl();
        if (targetUrl == null || targetUrl.isBlank()) {
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            logger.info("Wakeup ping {} -> {}", targetUrl, response.statusCode());
        } catch (Exception ex) {
            logger.warn("Wakeup ping failed for {}: {}", targetUrl, ex.getMessage());
        }
    }

    private String resolveTargetUrl() {
        String explicitUrl = System.getenv("WAKEUP_URL");
        if (explicitUrl != null && !explicitUrl.isBlank()) {
            return explicitUrl.trim();
        }

        String renderExternalUrl = System.getenv("RENDER_EXTERNAL_URL");
        if (renderExternalUrl == null || renderExternalUrl.isBlank()) {
            return null;
        }

        String base = renderExternalUrl.trim();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/students";
    }
}
