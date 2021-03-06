package com.ncu.pojo;

import java.util.Date;

public class Order {
    private Integer id;

    private Date orderDate;

    private Date sendDate;

    private Date overDate;

    private Integer orderState;

    private Double totalPrice;
    //1 校园自取  2送货上门
    private Integer getWay;

    private Integer payWay;

    private Integer userId;

    private Integer cropId;

    private String orderNumber;

    private String shippingAddr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getGetWay() {
        return getWay;
    }

    public void setGetWay(Integer getWay) {
        this.getWay = getWay;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getShippingAddr() {
        return shippingAddr;
    }

    public void setShippingAddr(String shippingAddr) {
        this.shippingAddr = shippingAddr == null ? null : shippingAddr.trim();
    }

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", sendDate="
				+ sendDate + ", overDate=" + overDate + ", orderState="
				+ orderState + ", totalPrice=" + totalPrice + ", getWay="
				+ getWay + ", payWay=" + payWay + ", userId=" + userId
				+ ", cropId=" + cropId + ", orderNumber=" + orderNumber
				+ ", shippingAddr=" + shippingAddr + "]";
	}
    
}