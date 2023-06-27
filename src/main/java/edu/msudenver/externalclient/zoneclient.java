package edu.msudenver.externalclient;

import edu.msudenver.externalclient.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class zoneclient {
    @Value("${zone.service.host}")
    private String zoneServiceHost;

    @Autowired
    private RestTemplate restTemplate;

    public PlayerResponse[] getPlayerList(Long zoneId, Long tileId, Long radius)
    {
        return restTemplate.getForObject(zoneServiceHost + "/zones/{1}/{2}?{3}", PlayerResponse[].class, zoneId, tileId, radius);
    }
}
