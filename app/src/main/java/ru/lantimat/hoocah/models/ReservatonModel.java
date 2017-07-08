package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lantimat on 08.07.17.
 */

@IgnoreExtraProperties
public class ReservatonModel {
    String tableId;
    boolean reservation;
    String clientName;
    long reservationTime;

    public ReservatonModel(String tableId, boolean reservation, String clientName, long reservationTime) {
        this.tableId = tableId;
        this.reservation = reservation;
        this.clientName = clientName;
        this.reservationTime = reservationTime;
    }

    public ReservatonModel() {
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(long reservationTime) {
        this.reservationTime = reservationTime;
    }
}
