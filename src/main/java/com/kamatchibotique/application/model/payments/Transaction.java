package com.kamatchibotique.application.model.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamatchibotique.application.enums.payment.PaymentType;
import com.kamatchibotique.application.enums.payment.TransactionType;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.order.Order;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "SM_TRANSACTION")
public class Transaction extends Auditable<String> {


    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TRANSACTION_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "TRANSACT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = true)
    private Order order;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "TRANSACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "TRANSACTION_TYPE")
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "PAYMENT_TYPE")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "DETAILS")
    private String details;

    @Transient
    private Map<String, String> transactionDetails = new HashMap<String, String>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getTransactionTypeName() {
        return this.getTransactionType() != null ? this.getTransactionType().name() : "";
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Map<String, String> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(Map<String, String> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String toJSONString() {

        if (this.getTransactionDetails() != null && this.getTransactionDetails().size() > 0) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this.getTransactionDetails());
            } catch (Exception e) {
                LOGGER.error("Cannot parse transactions map", e);
            }

        }

        return null;
    }

}
