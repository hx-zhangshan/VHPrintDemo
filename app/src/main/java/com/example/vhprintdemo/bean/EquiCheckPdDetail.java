package com.example.vhprintdemo.bean;

import java.math.BigDecimal;


public class EquiCheckPdDetail {

	private int checkId;
	private String checkCode;
	private int checkDetailId;
	private String equiArchId;
	private String equiArchCode;
	private String equiName;
	private String equiDictId;
	private String equiDictCode;
	private String equiDictName;
	private String equiBrand;
	private String equiModel;
	private int acctAmount;//账面数量
	private int amount;//盘点数量
	private int profitAmount;//盈亏数量
	private String pandlReason;
	private String barCode;
	private String selfCode;
	private int checkState;
	private String uploadPsn;
	private String uploadTime;
	private String isAddEquip;
	
	private BigDecimal price; //单价
	private String uploadPdaNo;

	
	private String compCode;
	private String copyCode;
	

	private BigDecimal pdprice; //盘点金额

	private BigDecimal ykprice; //盈亏金额
	private String checkStateApp;
	private String isDelete;
	
	public int getCheckId() {
		return checkId;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public int getCheckDetailId() {
		return checkDetailId;
	}

	public void setCheckDetailId(int checkDetailId) {
		this.checkDetailId = checkDetailId;
	}


	public String getEquiArchCode() {
		return equiArchCode;
	}

	public void setEquiArchCode(String equiArchCode) {
		this.equiArchCode = equiArchCode;
	}

	public String getEquiName() {
		return equiName;
	}

	public void setEquiName(String equiName) {
		this.equiName = equiName;
	}

	

	public String getEquiDictCode() {
		return equiDictCode;
	}

	public void setEquiDictCode(String equiDictCode) {
		this.equiDictCode = equiDictCode;
	}

	public String getEquiDictName() {
		return equiDictName;
	}

	public void setEquiDictName(String equiDictName) {
		this.equiDictName = equiDictName;
	}

	public String getEquiBrand() {
		return equiBrand;
	}

	public void setEquiBrand(String equiBrand) {
		this.equiBrand = equiBrand;
	}

	public String getEquiModel() {
		return equiModel;
	}

	public void setEquiModel(String equiModel) {
		this.equiModel = equiModel;
	}

	public int getAcctAmount() {
		return acctAmount;
	}

	public void setAcctAmount(int acctAmount) {
		this.acctAmount = acctAmount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(int profitAmount) {
		this.profitAmount = profitAmount;
	}

	public String getPandlReason() {
		return pandlReason;
	}

	public void setPandlReason(String pandlReason) {
		this.pandlReason = pandlReason;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSelfCode() {
		return selfCode;
	}

	public void setSelfCode(String selfCode) {
		this.selfCode = selfCode;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public String getUploadPsn() {
		return uploadPsn;
	}

	public void setUploadPsn(String uploadPsn) {
		this.uploadPsn = uploadPsn;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getIsAddEquip() {
		return isAddEquip;
	}

	public void setIsAddEquip(String isAddEquip) {
		this.isAddEquip = isAddEquip;
	}

	public String getEquiArchId() {
		return equiArchId;
	}

	public void setEquiArchId(String equiArchId) {
		this.equiArchId = equiArchId;
	}

	public String getEquiDictId() {
		return equiDictId;
	}

	public void setEquiDictId(String equiDictId) {
		this.equiDictId = equiDictId;
	}


	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getUploadPdaNo() {
		return uploadPdaNo;
	}

	public void setUploadPdaNo(String uploadPdaNo) {
		this.uploadPdaNo = uploadPdaNo;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getCopyCode() {
		return copyCode;
	}

	public void setCopyCode(String copyCode) {
		this.copyCode = copyCode;
	}

	public BigDecimal getPdprice() {
		return pdprice;
	}

	public void setPdprice(BigDecimal pdprice) {
		this.pdprice = pdprice;
	}

	public BigDecimal getYkprice() {
		return ykprice;
	}

	public void setYkprice(BigDecimal ykprice) {
		this.ykprice = ykprice;
	}

	public String getCheckStateApp() {
		return checkStateApp;
	}

	public void setCheckStateApp(String checkStateApp) {
		this.checkStateApp = checkStateApp;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

}
