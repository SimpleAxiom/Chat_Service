package edu.msudenver.chat.trade;

import edu.msudenver.chat.message.Message;
import edu.msudenver.chat.message.MessageService;
import edu.msudenver.chat.privateMessage.PrivateMessage;
import edu.msudenver.chat.privateMessage.PrivateMessageService;
import edu.msudenver.externalclient.model.InventoryItemResponse;
import edu.msudenver.externalclient.model.PlayerResponse;
import edu.msudenver.externalclient.playerclient;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/chat/trades")
public class TradeController {
    @Value("${chat.service.host}")
    private String chatServiceHost;
    @Autowired
    private TradeService tradeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private playerclient playerClient;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Trade>> getTrades() {
        return ResponseEntity.ok(tradeService.getTrades());
    }

    @GetMapping(path = "/{tradeId}", produces = "application/json")
    public ResponseEntity<Trade> getTrade(@PathVariable Long tradeId) {
        Trade trade = tradeService.getTrade(tradeId);
        return new ResponseEntity<>(trade, trade == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    /// Main Controller for initiating a trade
    /// Verify the player 1 and player 2 ids and the player 1 item quantity
    /// Send trade info to player 1 and player 2, player 2 should get link for trade as well
    /// Then send links to both players links for approval
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Trade> createTrade(@RequestBody Trade trade) {
        try {
            //Grab Player 1
            Long player1ID = trade.getPlayer1Id();
            PlayerResponse player1 = new PlayerResponse();// = playerClient.getPlayer(player1ID);

            player1.playerId = 1L;
            player1.Name = "Jeff";
            player1.isOnline = true;

            if(player1 == null)
                throw new Exception("Player 1 is not a valid trader.");

            //Check Player 1 ID for online and valid
            if(!player1.isOnline)
                throw new Exception("Player 1 is not online.");

            //Grab Player 2
            Long player2ID = trade.getPlayer2Id();
            PlayerResponse player2 = new PlayerResponse();// = playerClient.getPlayer(player2ID);

            player2.playerId = 2L;
            player2.Name = "Lisa";
            player2.isOnline = true;

            if(player2 == null)
                throw new Exception("Player 2 is not a valid trader.");

            //Check Player 2 ID for online and valid
            if(!player2.isOnline)
                throw new Exception("Player 2 is not online.");

            //Check that Player 1 item and quantity are real
            Long player1ItemId = trade.getItem1Id();
            Short player1Quantity = trade.getItem1Quantity();

            InventoryItemResponse player1Item = new InventoryItemResponse();// = playerClient.getInventoryItem(player1ID, player1ItemId);

            player1Item.Name = "Tool";
            player1Item.Quantity = 2;

            if(player1Item == null || player1Item.Quantity < player1Quantity)
                throw new Exception("Player 1 does not have that quantity of items.");

            //Cleanup all other parts of trade object so we only get the player 1/2 ids and player 1 item/qty
            trade.setTradeStatus((short) 0);
            trade.setItem2Id(0L);
            trade.setItem2Quantity((short) 0);
            trade.setPlayer1Approval(null);
            trade.setPlayer2Approval(null);

            //Save the trade and get the trade id created
            Trade tradeCreated;
            ResponseEntity<Trade> tradeResponseEntity = new ResponseEntity<>(tradeCreated = tradeService.saveTrade(trade), HttpStatus.CREATED);

            //If the trade didn't work (and didn't already throw an exception) then lets end it here
            if(tradeResponseEntity.getStatusCode() != HttpStatus.CREATED)
                throw new Exception("Trade could not be initiated");

            //Send a chat message to player 2 with the trade ID to make an offer
            String uri = chatServiceHost + "/trades/" + tradeCreated.getTradeId() + "/";
            String chatMessage = "Hi " + player2.Name + ", use this link to add your trade items: " + uri;

            Message message = new Message();
            PrivateMessage privateMessage = new PrivateMessage();
            message.setMessageBody(chatMessage);
            message.setDateTime(LocalDateTime.now());
            message.setSenderId(player1ID);
            message.setSenderType("PLAYER");
            message.setMessageType("TRADE");

            Message updatedMessage = messageService.saveMessage(message);
            privateMessage.setMessageId(updatedMessage.getMessageId());
            privateMessage.setReceiver_id(player2ID);

            //TODO do we need to do some checks on this?
            privateMessageService.savePrivateMessage(privateMessage);

            //Send a chat message to player 1 with an option to approve the trade
            //TODO create the uris with the trade id and approval info/
            String uriApprove = chatServiceHost + "/trades/" + tradeCreated.getTradeId() + "/" + player1ID + "?approve=true";
            String uriCancel = chatServiceHost + "/trades/" + tradeCreated.getTradeId() + "/" + player1ID + "?approve=false";

            chatMessage = "Hi " + player1.Name + ", use this link to approve of the trade: " + uriApprove + " or this one to cancel: " + uriCancel;

            message = new Message(); // If we dont create a new object, the old ID is still used
            message.setMessageBody(chatMessage);
            message.setDateTime(LocalDateTime.now());
            message.setSenderId(player1ID);
            message.setSenderType("PLAYER");
            message.setMessageType("TRADE");

            updatedMessage = messageService.saveMessage(message);
            privateMessage.setMessageId(updatedMessage.getMessageId());
            privateMessage.setReceiver_id(player1ID);

            //TODO do we need to do some checks on this?
            privateMessageService.savePrivateMessage(privateMessage);

            //Send a chat message to player 2 with an option to approve the trade
            //TODO create the uris with the trade id and approval info/
            uriApprove = chatServiceHost + "/trades/" + tradeCreated.getTradeId() + "/" + player2ID + "?approve=true";
            uriCancel = chatServiceHost + "/trades/" + tradeCreated.getTradeId() + "/" + player2ID + "?approve=false";

            //TODO use the players name to create a chat message
            chatMessage = "Hi " + player2.Name + ", use this link to approve of the trade: " + uriApprove + " or this one to cancel: " + uriCancel;

            message = new Message(); // If we dont create a new object, the old ID is still used
            message.setMessageBody(chatMessage);
            message.setDateTime(LocalDateTime.now());
            message.setSenderId(player1ID);
            message.setSenderType("PLAYER");
            message.setMessageType("TRADE");

            updatedMessage = messageService.saveMessage(message);
            privateMessage.setMessageId(updatedMessage.getMessageId());
            privateMessage.setReceiver_id(player2ID);

            //TODO do we need to do some checks on this?
            privateMessageService.savePrivateMessage(privateMessage);

            return tradeResponseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{tradeId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Trade> updateTrade(@PathVariable Long tradeId, @RequestBody Trade updatedTrade) {

        try {
            Trade trade = tradeService.getTrade(tradeId);

            if (trade == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            //Grab Player 1
            Long player1ID = trade.getPlayer1Id();
            PlayerResponse player1 = new PlayerResponse(); // = playerClient.getPlayer(player1ID);

            player1.Name = "Jeff";
            player1.isOnline = true;
            player1.playerId = 1L;
            if(player1 == null)
                throw new Exception("Player 1 is not a valid trader.");

            //Check Player 1 ID for online and valid
            if(!player1.isOnline)
                throw new Exception("Player 1 is not online.");

            //Grab Player 2
            Long player2ID = trade.getPlayer2Id();
            PlayerResponse player2 = new PlayerResponse();// = playerClient.getPlayer(player2ID);

            player2.playerId = 2L;
            player2.Name = "Lisa";
            player2.isOnline = true;

            if(player2 == null)
                throw new Exception("Player 2 is not a valid trader.");

            //Check Player 2 ID for online and valid
            if(!player2.isOnline)
                throw new Exception("Player 2 is not online.");

            //Check that Player 2 item and quantity are real
            Long player2ItemId = trade.getItem2Id();
            Short player2Quantity = trade.getItem2Quantity();

            InventoryItemResponse player2Item = new InventoryItemResponse(); // = playerClient.getInventoryItem(player2ID, player2ItemId);

            player2Item.Name = "Money";
            player2Item.Quantity = 20;

            if(player2Item == null || player2Item.Quantity < player2Quantity)
                throw new Exception("Player 2 does not have that quantity of items.");

            //TODO actually make an update
            trade.setItem2Id(updatedTrade.getItem2Id());
            trade.setItem2Quantity(updatedTrade.getItem2Quantity());
            trade.setPlayer1Approval(null);
            trade.setPlayer2Approval(null);

            //TODO send the approval/cancel links again
            String uriApprove = chatServiceHost + "/trades/" + trade.getTradeId() + "/" + player1ID + "?approve=true";
            String uriCancel = chatServiceHost + "/trades/" + trade.getTradeId() + "/" + player1ID + "?approve=false";

            String chatMessage = "Hi " + player1.Name + ", use this link to approve of the trade: " + uriApprove + " or this one to cancel: " + uriCancel;

            Message message = new Message(); // If we dont create a new object, the old ID is still used
            message.setMessageBody(chatMessage);
            message.setDateTime(LocalDateTime.now());
            message.setSenderId(player1ID);
            message.setSenderType("PLAYER");
            message.setMessageType("TRADE");

            Message updatedMessage = messageService.saveMessage(message);
            PrivateMessage privateMessage = new PrivateMessage();
            privateMessage.setMessageId(updatedMessage.getMessageId());
            privateMessage.setReceiver_id(player1ID);

            //TODO do we need to do some checks on this?
            privateMessageService.savePrivateMessage(privateMessage);

            //Send a chat message to player 2 with an option to approve the trade
            //TODO create the uris with the trade id and approval info/
            uriApprove = chatServiceHost + "/trades/" + trade.getTradeId() + "/" + player2ID + "?approve=true";
            uriCancel = chatServiceHost + "/trades/" + trade.getTradeId() + "/" + player2ID + "?approve=false";

            //TODO use the players name to create a chat message
            chatMessage = "Hi " + player2.Name + ", use this link to approve of the trade: " + uriApprove + " or this one to cancel: " + uriCancel;

            message = new Message(); // If we dont create a new object, the old ID is still used
            message.setMessageBody(chatMessage);
            message.setDateTime(LocalDateTime.now());
            message.setSenderId(player1ID);
            message.setSenderType("PLAYER");
            message.setMessageType("TRADE");

            updatedMessage = messageService.saveMessage(message);
            privateMessage.setMessageId(updatedMessage.getMessageId());
            privateMessage.setReceiver_id(player2ID);

            //TODO do we need to do some checks on this?
            privateMessageService.savePrivateMessage(privateMessage);

            return ResponseEntity.ok(tradeService.saveTrade(trade));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(path = "/{tradeId}/{playerId}", produces = "application/json")
    public ResponseEntity<String> approveTrade(@PathVariable Long tradeId,@PathVariable Long playerId,
                                          @RequestParam Boolean approval) {

        try {
            Trade trade = tradeService.getTrade(tradeId);

            //Cant trade if its not a real trade ID
            if(trade == null)
                return new ResponseEntity<>("Invalid Trade ID: " + tradeId,HttpStatus.NOT_FOUND);

            //if trade status says the trade already completed
            if(trade.getTradeStatus() != null && trade.getTradeStatus() != 0)
                return new ResponseEntity<>("Trade no longer allowed.", HttpStatus.NOT_ACCEPTABLE);

            //make sure the player Id being used is found
            if(playerId == trade.getPlayer1Id())
                trade.setPlayer1Approval(approval);
            else if(playerId == trade.getPlayer2Id())
                trade.setPlayer2Approval(approval);
            else
                return new ResponseEntity<>("Invalid Player ID: " + playerId,HttpStatus.BAD_REQUEST);

            //check if approvals are now both set to make the trade
            if(trade.getPlayer1Approval() == false || trade.getPlayer2Approval() == false)
            {
                //TODO fail the trade

                //TODO send chats to players
                String chatMessage = "Trade " + trade.getTradeId() + " cancelled between player " + trade.getPlayer1Id() + " and player " + trade.getPlayer2Id();

                Message message = new Message(); // If we dont create a new object, the old ID is still used
                message.setDateTime(LocalDateTime.now());
                message.setMessageBody(chatMessage);
                message.setSenderId(playerId);
                message.setSenderType("PLAYER");
                message.setMessageType("TRADE");

                Message updatedMessage = messageService.saveMessage(message);
                PrivateMessage privateMessage = new PrivateMessage();
                privateMessage.setMessageId(updatedMessage.getMessageId());
                privateMessage.setReceiver_id(trade.getPlayer1Id());

                //TODO do we need to do some checks on this?
                privateMessageService.savePrivateMessage(privateMessage);

                privateMessage.setReceiver_id(trade.getPlayer2Id());
                privateMessageService.savePrivateMessage(privateMessage);

                trade.setTradeStatus((short)2);
            }
            else if(trade.getPlayer1Approval() == true && trade.getPlayer2Approval() == true)
            {
                //TODO make the trade

                //TODO send chats to players
                String chatMessage = "Trade " + trade.getTradeId() + " succeeded between player " + trade.getPlayer1Id() + " and player " + trade.getPlayer2Id();

                Message message = new Message(); // If we dont create a new object, the old ID is still used
                message.setMessageBody(chatMessage);
                message.setDateTime(LocalDateTime.now());
                message.setSenderId(playerId);
                message.setSenderType("PLAYER");
                message.setMessageType("TRADE");

                Message updatedMessage = messageService.saveMessage(message);
                PrivateMessage privateMessage = new PrivateMessage();
                privateMessage.setMessageId(updatedMessage.getMessageId());
                privateMessage.setReceiver_id(trade.getPlayer1Id());

                //TODO do we need to do some checks on this?
                privateMessageService.savePrivateMessage(privateMessage);

                privateMessage.setReceiver_id(trade.getPlayer2Id());
                privateMessageService.savePrivateMessage(privateMessage);

                trade.setTradeStatus((short)1);
            }
            else
                trade.setTradeStatus((short)0);

            tradeService.saveTrade(trade);
            return new ResponseEntity<>("Trade set to " + trade.getTradeStatus(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }

    }
}
