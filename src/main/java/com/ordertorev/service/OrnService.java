package com.ordertorev.service;

import java.util.Date;
import java.util.List;

import com.ordertorev.model.AccountInfo;

public interface OrnService {

	public List<AccountInfo> readExcel(String excelPath);
	
	public void processAccountInfo(List<AccountInfo> accountInfos);
	
	public void processAccountInfonullRows(List<AccountInfo> accountInfos);
	
	public Date getPaymentDateFromFlipkart(int firstDay, int lastDay, int paymentDay, Date orderDeliveryDate);
	
}
