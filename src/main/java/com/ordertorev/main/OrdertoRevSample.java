package com.ordertorev.main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.ordertorev.model.AccountInfo;
import com.ordertorev.service.OrnService;
import com.ordertorev.serviceImpl.OrnServiceImpl;

public class OrdertoRevSample {

	OrnService ornService = new OrnServiceImpl();
	
	public OrdertoRevSample() {
		try {
			ordertorevAssignment1();
			ordertorevAssignment2();
			ordertorevAssignment3();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ordertorevAssignment2() throws Exception {
		System.out.println("===============ordertorevAssignment2=================>");
		
		List<AccountInfo> accountInfos = ornService.readExcel("C:/temp/ordertorevTem.xlsx");
		ornService.processAccountInfo(accountInfos);
	}
	
	public void ordertorevAssignment3() throws Exception {
		System.out.println("===============ordertorevAssignment3==================>");
		List<AccountInfo> accountInfos = ornService.readExcel("C:/temp/ordertorevTem.xlsx");
		ornService.processAccountInfonullRows(accountInfos);
		
	}
	public void ordertorevAssignment1() throws Exception {
		System.out.println("================ordertorevAssignment1=================>");
		Date paymentDate = ornService.getPaymentDateFromFlipkart(5, 10, 15, new GregorianCalendar(2015, Calendar.SEPTEMBER, 18).getTime());
		System.out.println("Payment Date From Flipkart: "+paymentDate);
		
	}
	public static void main(String[] args) {
		new OrdertoRevSample();
	}
	
	
}
