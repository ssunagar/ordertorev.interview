package com.ordertorev.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ordertorev.constants.OrnConstants;
import com.ordertorev.model.AccountInfo;
import com.ordertorev.service.OrnService;

public class OrnServiceImpl implements OrnService {

	public List<AccountInfo> readExcel(String excelPath) {
		List<AccountInfo> accountInfosList = null;
		try {
			accountInfosList = new ArrayList<AccountInfo>();
			FileInputStream inputStream = new FileInputStream(new File(excelPath));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			int rowNumber = 1;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				AccountInfo accountInfo = new AccountInfo();
				if (rowNumber > 1) {
					Iterator<Cell> cellIterator = nextRow.cellIterator();
					int cellIndex = 1;
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cellIndex) {
							case 1: {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									accountInfo.setAccoutId((int) cell
											.getNumericCellValue());
								}
								break;
							}
							case 2: {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									accountInfo.setAccoutName(cell
											.getStringCellValue());
								}
								break;
							}
							case 3: {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									accountInfo.setAccoutDesc(cell
											.getStringCellValue());
								}
								break;
							}
							case 4: {
								if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									accountInfo.setAmount(cell
											.getNumericCellValue());
								}
								break;
							}
						}

						cellIndex++;
					}
					accountInfosList.add(accountInfo);
				}

				rowNumber++;
			}
			inputStream.close();
		} catch (Exception expObj) {
			expObj.printStackTrace();
		}
		return accountInfosList;
	}

	public void processAccountInfo(List<AccountInfo> accountInfos) {
		Map<Integer, Double> accountInfoMap = new HashMap<Integer, Double>();
		try {
			for (AccountInfo accountInfo : accountInfos) {
				if (accountInfoMap.containsKey(accountInfo.getAccoutId())) {
					accountInfoMap.put(accountInfo.getAccoutId(),
							accountInfoMap.get(accountInfo.getAccoutId())
									+ accountInfo.getAmount());
				} else {
					accountInfoMap.put(accountInfo.getAccoutId(),
							accountInfo.getAmount());
				}
			}
			System.out.println("============Account Details============>");
			if (null != accountInfoMap || accountInfoMap.size() > 0) {
				for (Map.Entry<Integer, Double> entry : accountInfoMap
						.entrySet()) {
					System.out.println(entry.getKey() + " : "+ entry.getValue());
				}
			} else {
				System.out.println("Please add some Data in the provided excel and then try");
			}
		} catch (Exception expObj) {
			expObj.printStackTrace();
		}
	}

	public void processAccountInfonullRows(List<AccountInfo> accountInfos) {
		List<AccountInfo> accountInfosWithnull = new ArrayList<AccountInfo>();
		List<AccountInfo> accountInfosWithNonull = new ArrayList<AccountInfo>();
		for (AccountInfo accountInfo : accountInfos) {
			if(accountInfo.getAccoutName().equals("null") || accountInfo.getAccoutDesc().equals("null")) {
				accountInfosWithnull.add(accountInfo);
			} else {
				accountInfosWithNonull.add(accountInfo);
			}
		}
		
		System.out.println("============Account Details with null============>");
		for (AccountInfo accountInfo : accountInfosWithnull) {
			System.out.println(accountInfo.getAccoutId() + " : " + accountInfo.getAccoutName() + " : " + accountInfo.getAccoutDesc() + " : " + accountInfo.getAmount());
		}
		
		System.out.println("============Account Details with No null============>");
		for (AccountInfo accountInfo : accountInfosWithNonull) {
			System.out.println(accountInfo.getAccoutId() + " : " + accountInfo.getAccoutName() + " : " + accountInfo.getAccoutDesc() + " : " + accountInfo.getAmount());
		}
	}
	
	public Date getPaymentDateFromFlipkart(int firstDay, int lastDay, int paymentDay, Date orderDeliveryDate) {
		Date paymentDate = null;
		boolean paymentDateFlag = false;
		try {
			Calendar paymentDateCal = Calendar.getInstance();
			paymentDateCal.setTime(orderDeliveryDate);
			Calendar orderDeliveryDateCal = Calendar.getInstance();
			orderDeliveryDateCal.setTime(orderDeliveryDate);
			Calendar firstDayDate = Calendar.getInstance();
			firstDayDate.setTime(orderDeliveryDate);
			firstDayDate.set(Calendar.DATE, firstDay);
			Calendar lastDayDate = Calendar.getInstance();
			lastDayDate.setTime(orderDeliveryDate);
			lastDayDate.set(Calendar.DATE, lastDay);
			
			while (!paymentDateFlag) {
				if(orderDeliveryDateCal.before(lastDayDate) && orderDeliveryDateCal.after(firstDayDate)) {
					paymentDateCal.set(Calendar.DATE, lastDayDate.get(Calendar.DATE) + (lastDay - firstDay));
					paymentDateFlag = true;
				} else {
					firstDayDate.set(Calendar.DATE, lastDayDate.get(Calendar.DATE) + OrnConstants.DAYINCREAMENT);
					lastDayDate.set(Calendar.DATE, lastDayDate.get(Calendar.DATE) + (lastDay - firstDay) + OrnConstants.DAYINCREAMENT);
				}
			}
			if(paymentDateFlag) {
				paymentDate = paymentDateCal.getTime(); 
			} else {
				System.out.println("Provide order Delivery Datel");
			}
			
		} catch (Exception expObj) {
			expObj.printStackTrace();
		}
		return paymentDate;
	}
	
	public static long daysBetween(Calendar startDate, Calendar endDate) {
	    long end = endDate.getTimeInMillis();
	    long start = startDate.getTimeInMillis();
	    return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
	}
	
	public static void main(String[] args) {
		System.out.println("result:"+new OrnServiceImpl().getPaymentDateFromFlipkart(5, 10, 15, new GregorianCalendar(2015, Calendar.SEPTEMBER, 18).getTime()));
	}
	
}
