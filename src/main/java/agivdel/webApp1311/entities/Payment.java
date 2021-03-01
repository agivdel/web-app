package agivdel.webApp1311.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int userId;
    private Long payment;
    private Timestamp paymentTime;

    public Payment(int userId, Long payment) {
        this.userId = userId;
        this.payment = payment;
    }

    public Payment(int id, int userId, Long payment, Timestamp paymentTime) {
        this.id = id;
        this.userId = userId;
        this.payment = payment;
        this.paymentTime = paymentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getPayment() {
        return payment;
    }

    public void setPayment(Long payment) {
        this.payment = payment;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", userId=" + userId +
                ", payment=" + payment +
                ", paymentTime=" + paymentTime +
                '}';
    }
}