package com.cheuks.bin.original.test.tolist;

import java.math.BigDecimal;

public class MPreCertificateRowDetail {

	private String invoiceLId;
	private String segmentCombination;
	private String segmentNameCombination;
	private String debitCreditType;
	private BigDecimal payAmount;
	@Ignore
	private MPreCertificateHead mPreCertificateHead;
	public String getInvoiceLId() {
		return invoiceLId;
	}
	public MPreCertificateRowDetail setInvoiceLId(String invoiceLId) {
		this.invoiceLId = invoiceLId;
		return this;
	}
	public String getSegmentCombination() {
		return segmentCombination;
	}
	public MPreCertificateRowDetail setSegmentCombination(String segmentCombination) {
		this.segmentCombination = segmentCombination;
		return this;
	}
	public String getSegmentNameCombination() {
		return segmentNameCombination;
	}
	public MPreCertificateRowDetail setSegmentNameCombination(String segmentNameCombination) {
		this.segmentNameCombination = segmentNameCombination;
		return this;
	}
	public String getDebitCreditType() {
		return debitCreditType;
	}
	public MPreCertificateRowDetail setDebitCreditType(String debitCreditType) {
		this.debitCreditType = debitCreditType;
		return this;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public MPreCertificateRowDetail setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
		return this;
	}
	public MPreCertificateHead getmPreCertificateHead() {
		return mPreCertificateHead;
	}
	public MPreCertificateRowDetail setmPreCertificateHead(MPreCertificateHead mPreCertificateHead) {
		this.mPreCertificateHead = mPreCertificateHead;
		return this;
	}
	public MPreCertificateRowDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MPreCertificateRowDetail(String invoiceLId, String segmentCombination, String segmentNameCombination, String debitCreditType, BigDecimal payAmount, MPreCertificateHead mPreCertificateHead) {
		super();
		this.invoiceLId = invoiceLId;
		this.segmentCombination = segmentCombination;
		this.segmentNameCombination = segmentNameCombination;
		this.debitCreditType = debitCreditType;
		this.payAmount = payAmount;
		this.mPreCertificateHead = mPreCertificateHead;
	}

}
