package edu.msudenver.chat.trade;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @Column(name = "trade_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    @Column(name = "player1_id")
    @NotNull(message = "player1_id cannot be null")
    private Long player1Id;

    @Column(name = "player2_id")
    @NotNull(message = "player2_id cannot be null")
    private Long player2Id;

    @Column(name = "item1_id")
    private Long item1Id;

    @Column(name = "item2_id")
    private Long item2Id;

    @Column(name = "item1_quantity")
    private Short item1Quantity;

    @Column(name = "item2_quantity")
    private Short item2Quantity;

    @Column(name = "player1_approval")
    private Boolean player1Approval;

    @Column(name = "player2_approval")
    private Boolean player2Approval;

    @Column(name = "trade_status")
    private Short tradeStatus;

    public Trade() {
    }

    public Trade(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Long getItem1Id() {
        return item1Id;
    }

    public void setItem1Id(Long item1Id) {
        this.item1Id = item1Id;
    }

    public Long getItem2Id() {
        return item2Id;
    }

    public void setItem2Id(Long item2Id) {
        this.item2Id = item2Id;
    }

    public Short getItem1Quantity() {
        return item1Quantity;
    }

    public void setItem1Quantity(Short item1Quantity) {
        this.item1Quantity = item1Quantity;
    }

    public Short getItem2Quantity() {
        return item2Quantity;
    }

    public void setItem2Quantity(Short item2Quantity) {
        this.item2Quantity = item2Quantity;
    }

    public Boolean getPlayer1Approval() {
        return player1Approval;
    }

    public void setPlayer1Approval(Boolean player1Approval) {
        this.player1Approval = player1Approval;
    }

    public Boolean getPlayer2Approval() {
        return player2Approval;
    }

    public void setPlayer2Approval(Boolean player2Approval) {
        this.player2Approval = player2Approval;
    }

    public Short getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Short tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}
