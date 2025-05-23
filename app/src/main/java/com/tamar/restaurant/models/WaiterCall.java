package com.tamar.restaurant.models;

import com.google.gson.annotations.SerializedName;

public class WaiterCall {
    @SerializedName("id")
    private Long id;

    @SerializedName("assignment")
    private WaiterAssignment assignment;

    @SerializedName("call_type")
    private CallType callType;

    @SerializedName("status")
    private OrderStatus status;

    //Getters and setters


    public Long getId() {
        return id;
    }

    public WaiterAssignment getAssignment(){
        return assignment;
    }

    public CallType getCallType() {
        return callType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAssignment(WaiterAssignment assignment) {
        this.assignment = assignment;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
