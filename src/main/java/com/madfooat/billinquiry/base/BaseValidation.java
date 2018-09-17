package com.madfooat.billinquiry.base;

import com.madfooat.billinquiry.domain.Bill;
import com.madfooat.billinquiry.exceptions.InvalidBillInquiryResponse;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseValidation {

    public boolean checkDate(Date date) throws InvalidBillInquiryResponse {

        if (date.getTime() < Calendar.getInstance().getTimeInMillis()) {
            return true;
        } else {
            throw new InvalidBillInquiryResponse();
        }
    }

    public boolean checkAmount(BigDecimal amount) throws InvalidBillInquiryResponse {

        if (amount != null) {
            return checkCurrancyFormat(amount.toString());
        } else {
            throw new InvalidBillInquiryResponse();
        }
    }

    public boolean checkFees(BigDecimal fees,BigDecimal amount) throws InvalidBillInquiryResponse {

        if (fees != null && (fees.compareTo(amount)<0)) {
            return checkCurrancyFormat(fees.toString());
        } else {
            return false;
        }

    }

    public boolean checkCurrancyFormat(String amount) throws InvalidBillInquiryResponse {

        int intIndx = amount.indexOf('.');
        int decimalIndx = amount.length() - intIndx - 1;
        if (decimalIndx <= 3) {
            return true;
        } else {
            throw new InvalidBillInquiryResponse();
        }
    }

    public Date checkDateFormat(String date) {
        Date dDate = null;
        DateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);
        try {
            dDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dDate;
    }

    public String correctDateFormat(String date){

        String day,monthe,year;
        day = date.substring(0,2);
        monthe = date.substring(3,5);
        year= date.substring(6);

        date = year+'-'+monthe+'-'+day;

        return date;
    }

    public  String fixDateFormat(String Response){
        for (int i = -1; (i = Response.indexOf("dueDate", i + 1)) != -1; i++) {
            String prevDate =  Response.substring(i+11,i+21);
            String newDate =  correctDateFormat(prevDate);
            Response = Response.replace(prevDate,newDate);
        }
        return Response;
    }

    public void validator(List<Bill> bills) {
        for (Bill bill : bills) {
            checkDate(bill.getDueDate());
            checkAmount(bill.getDueAmount());
            checkFees(bill.getFees(),bill.getDueAmount());
        }
    }

}
