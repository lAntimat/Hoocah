package ru.lantimat.hoocah.models;

/**
 * Created by lantimat on 19.06.17.
 */

public class TableModel {
    int id;
    boolean reservation;
    boolean free;
    int activeOrderId;
    long reservationTime;

    public TableModel(int id, int activeOrderId, boolean free, boolean reservation, long reservationTime) {
        this.id = id;
        this.reservation = reservation;
        this.free = free;
        this.activeOrderId = activeOrderId;
        this.reservationTime = reservationTime;
    }

    public TableModel(int id, int activeOrderId, boolean free) {
        this.id = id;
        this.free = free;
        this.activeOrderId = activeOrderId;
    }



    public TableModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public int getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(int activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public long getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(long reservationTime) {
        this.reservationTime = reservationTime;
    }
}
