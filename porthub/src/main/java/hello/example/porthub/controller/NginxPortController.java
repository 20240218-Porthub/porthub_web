package hello.example.porthub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class NginxPortController {
    private final Environment env;

    @GetMapping("/port")
    public String port() {
        List<String> ports = Arrays.asList(env.getActiveProfiles());
        List<String> realPorts = Arrays.asList("real", "real1", "real2");
        String defaultPort = ports.isEmpty() ? "default" : ports.get(0);

        return ports.stream().filter(realPorts::contains).findAny().orElse(defaultPort);
    }
}
