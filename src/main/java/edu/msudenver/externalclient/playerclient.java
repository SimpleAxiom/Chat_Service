package edu.msudenver.externalclient;

import edu.msudenver.externalclient.model.InventoryItemResponse;
import edu.msudenver.externalclient.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class playerclient {
    //i.e https://cityservice.heroku.com
    @Value("${player.service.host}")
    private String playerServiceHost;

    @Autowired
    private RestTemplate restTemplate;

    public PlayerResponse getPlayer(Long playerId)
    {
        return restTemplate.getForObject(playerServiceHost + "/players/{1}", PlayerResponse.class, playerId);
    }

    public InventoryItemResponse getInventoryItem(Long playerId, Long itemId)
    {
        return restTemplate.getForObject(playerServiceHost + "/players/{1}/inventory/{2}", InventoryItemResponse.class, playerId,itemId);
    }

    //public Long[] getAllPlayerIDs(int radius)
    //{
    //    return restTemplate.getForObject(privateChatServiceHost + "/players?radius={1}", Long[].class, radius);
    //}

    //public Long postPlayerID(PlayerRequest playerRequest)
    //{
    //    return restTemplate.postForObject(privateChatServiceHost + "/players/", playerRequest,Long.class);
    //}

}

//added private.chat.service.host to the application-local.properties file
//Take the request and response json objects and codify them from the internet (convert json to java)
//put these in a models.response and models.request package

//autowire this class into or controller