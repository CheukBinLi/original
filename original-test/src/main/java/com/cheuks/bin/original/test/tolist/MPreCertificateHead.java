package com.cheuks.bin.original.test.tolist;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MPreCertificateHead {

	private String invoiceId;
	private String sourceBillCode;
	private String invoiceCode;
	private String companyName;
	private BigDecimal invoiceAmount;
	private BigDecimal totalAmount;
	private BigDecimal sumRepayAmount;
	private String currencyName;
	private String reasonDesc;
	private String bizDesc;
	private String vendorName;
	private String vendorSiteName;
	private String budgetName;
	private Date glDate;
	private String bizFlag;
	private String orderStatus;
	private String impErpType;
	private String importErpStatus;
	private String docSequenceValue;
	private String impErpMes;
	private List<MPreCertificateRowDetail> mPreCertificateRowDetails;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSourceBillCode() {
		return sourceBillCode;
	}

	public void setSourceBillCode(String sourceBillCode) {
		this.sourceBillCode = sourceBillCode;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getSumRepayAmount() {
		return sumRepayAmount;
	}

	public void setSumRepayAmount(BigDecimal sumRepayAmount) {
		this.sumRepayAmount = sumRepayAmount;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getBizDesc() {
		return bizDesc;
	}

	public void setBizDesc(String bizDesc) {
		this.bizDesc = bizDesc;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorSiteName() {
		return vendorSiteName;
	}

	public void setVendorSiteName(String vendorSiteName) {
		this.vendorSiteName = vendorSiteName;
	}

	public String getBudgetName() {
		return budgetName;
	}

	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}

	public Date getGlDate() {
		return glDate;
	}

	public void setGlDate(Date glDate) {
		this.glDate = glDate;
	}

	public String getBizFlag() {
		return bizFlag;
	}

	public void setBizFlag(String bizFlag) {
		this.bizFlag = bizFlag;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getImpErpType() {
		return impErpType;
	}

	public void setImpErpType(String impErpType) {
		this.impErpType = impErpType;
	}

	public String getImportErpStatus() {
		return importErpStatus;
	}

	public void setImportErpStatus(String importErpStatus) {
		this.importErpStatus = importErpStatus;
	}

	public String getDocSequenceValue() {
		return docSequenceValue;
	}

	public void setDocSequenceValue(String docSequenceValue) {
		this.docSequenceValue = docSequenceValue;
	}

	public String getImpErpMes() {
		return impErpMes;
	}

	public void setImpErpMes(String impErpMes) {
		this.impErpMes = impErpMes;
	}
}
