package com.madfooat.billinquiry;

import com.google.gson.Gson;
import com.madfooat.billinquiry.base.BaseValidation;
import com.madfooat.billinquiry.domain.Bill;
import com.madfooat.billinquiry.exceptions.InvalidBillInquiryResponse;
import java.util.Arrays;
import java.util.List;

public class JSONParseBillInquiryResponse implements ParseBillInquiryResponse {
    @Override
    public List<Bill> parse(String billerResponse) throws InvalidBillInquiryResponse {
        // Write your implementation
        BaseValidation baseValidation = new BaseValidation();

        billerResponse = baseValidation.fixDateFormat(billerResponse);
        Bill[] billerResponseList = new Gson().fromJson(billerResponse, Bill[].class);
        List<Bill> billList = Arrays.asList(billerResponseList);

        baseValidation.validator(billList);
        return billList;
    }
}
