package com.yayawan.sdk.bean;

/**
 * 支付方式
 * @author wjy
 *
 */
public class PayMethod {

    public String payName;
   // public int iconId;
    public int mentid;
    public  String id;
    public String reason;
    public int status;
    public int discount;
    public int per;
    public String icon;
    public String sub_text;
    
    public int getPer() {
		return per;
	}
	public void setPer(int per) {
		this.per = per;
	}

	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSub_text() {
		return sub_text;
	}
	public void setSub_text(String sub_text) {
		this.sub_text = sub_text;
	}
	public PayMethod() {
        super();
        
    }
    public PayMethod(String payName,  int mentid) {
        super();
        this.payName = payName;
        //this.iconId = iconId;
        this.mentid = mentid;
    }
    
    public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public int getMentid() {
		return mentid;
	}

	public void setMentid(int mentid) {
		this.mentid = mentid;
	}

	

	@Override
	public String toString() {
		return "PayMethod [payName=" + payName + ", mentid=" + mentid + ", id="
				+ id + ", reason=" + reason + ", status=" + status
				+ ", discount=" + discount + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	
    
    
}
