package com.lupay.demoui.utils;

import java.util.HashMap;

public class ResponseCode {
    private static HashMap<String, CodeDetails> CodeTable = new HashMap<>();

    public static void setCodeResponses() {
        CodeTable.put("00", new CodeDetails("Approved or completed successfully", true));
        CodeTable.put("01", new CodeDetails("Refer to card issuer", true));
        CodeTable.put("02", new CodeDetails("Refer to special conditions for card issuer"));
        CodeTable.put("03", new CodeDetails("Invalid merchant"));
        CodeTable.put("04", new CodeDetails("Pick-up card"));
        CodeTable.put("05", new CodeDetails("Do not honor"));
        CodeTable.put("06", new CodeDetails("Error"));
        CodeTable.put("07", new CodeDetails("Pick-up card, special condition"));
        CodeTable.put("09", new CodeDetails("Request in progress (duplicate)"));
        CodeTable.put("10", new CodeDetails("Approved partial", true));
        CodeTable.put("11", new CodeDetails("Approved (VIP) *", true));
        CodeTable.put("12", new CodeDetails("Invalid transaction"));
        CodeTable.put("13", new CodeDetails("Invalid amount"));
        CodeTable.put("14", new CodeDetails("Invalid card number (no such number)"));
        CodeTable.put("15", new CodeDetails("No such issuer"));
        CodeTable.put("30", new CodeDetails("Format error"));
        CodeTable.put("31", new CodeDetails("Bank not supported by switch"));
        CodeTable.put("33", new CodeDetails("Expired card"));
        CodeTable.put("34", new CodeDetails("Suspected fraud"));
        CodeTable.put("35", new CodeDetails("Card acceptor contact acquirer"));
        CodeTable.put("36", new CodeDetails("Restricted card"));
        CodeTable.put("37", new CodeDetails("Card acceptor call acquirer security"));
        CodeTable.put("38", new CodeDetails("Allowable PIN tries exceded"));
        CodeTable.put("39", new CodeDetails("No credit account"));
        CodeTable.put("41", new CodeDetails("Lost card"));
        CodeTable.put("43", new CodeDetails("Stolen card, pick-up"));
        CodeTable.put("51", new CodeDetails("Not sufficient funds"));
        CodeTable.put("54", new CodeDetails("Expired card"));
        CodeTable.put("55", new CodeDetails("Incorrect personal identification number *"));
        CodeTable.put("56", new CodeDetails("No card record"));
        CodeTable.put("57", new CodeDetails("Transaction not permitted to cardholder"));
        CodeTable.put("58", new CodeDetails("Transaction not permitted to terminal"));
        CodeTable.put("61", new CodeDetails("Exceeds withdrawal amount limit"));
        CodeTable.put("62", new CodeDetails("Restricted card"));
        CodeTable.put("65", new CodeDetails("Exceeds withdrawal frequency limit"));
        CodeTable.put("68", new CodeDetails("Response received too late"));
        CodeTable.put("75", new CodeDetails("Allowable number of PIN tries exceeded *"));
        CodeTable.put("76", new CodeDetails("Reserved for private use or Approved country club *"));
        CodeTable.put("77", new CodeDetails("Reserved for private use or Approved pending identification (sign paper draft)*"));
        CodeTable.put("78", new CodeDetails("Reserved for private use or Approved blind *"));
        CodeTable.put("79", new CodeDetails("Reserved for private use or Approved administrative transaction *"));
        CodeTable.put("80", new CodeDetails("Reserved for private use or Approved national negative file hit OK *"));
        CodeTable.put("81", new CodeDetails("Reserved for private use or Approved commercial *"));
        CodeTable.put("82", new CodeDetails("Reserved for private use or No security module"));
        CodeTable.put("83", new CodeDetails("Reserved for private use or No accounts"));
        CodeTable.put("84", new CodeDetails("Reserved for private use or No PBF *"));
        CodeTable.put("85", new CodeDetails("Reserved for private use or PBF update error *"));
        CodeTable.put("86", new CodeDetails("Reserved for private use or Invalid authorization type"));
        CodeTable.put("87", new CodeDetails("Reserved for private use or Bad Track Data"));
        CodeTable.put("88", new CodeDetails("Reserved for private use or PTLF error *"));
        CodeTable.put("89", new CodeDetails("Reserved for private use or Invalid route service"));
        CodeTable.put("90", new CodeDetails("Cutoff is in process, a switch is ending business for a day and starting the next (transaction can be sent again in a few minutes)"));
        CodeTable.put("91", new CodeDetails("Issuer or switch is inoperative"));
        CodeTable.put("92", new CodeDetails("Financial institution or intermediate network facility cannot be found for routing"));
        CodeTable.put("94", new CodeDetails("Duplicate transmission"));
        CodeTable.put("96", new CodeDetails("Transaction not permitted, please contact the administrator."));
        CodeTable.put("N0", new CodeDetails("Reserved for private use or Unable to authorize"));
        CodeTable.put("N1", new CodeDetails("Reserved for private use or Invalid PAN length"));
        CodeTable.put("N2", new CodeDetails("Reserved for private use or Preauthorization full"));
        CodeTable.put("N3", new CodeDetails("Reserved for private use or Maximum online refund reached"));
        CodeTable.put("N4", new CodeDetails("Reserved for private use or Maximum offline refund reached"));
        CodeTable.put("N5", new CodeDetails("Reserved for private use or Maximum credit per refund"));
        CodeTable.put("N6", new CodeDetails("Reserved for private use or Maximum refund credit reached"));
        CodeTable.put("N7", new CodeDetails("Reserved for private use or Customer selected negative file reason"));
        CodeTable.put("N8", new CodeDetails("Reserved for private use or Over floor limit"));
        CodeTable.put("N9", new CodeDetails("Reserved for private use or Maximum number refund credits"));
        CodeTable.put("O0", new CodeDetails("Reserved for private use or Referral file full"));
        CodeTable.put("O1", new CodeDetails("Reserved for private use or NEG file problem *"));
        CodeTable.put("O2", new CodeDetails("Reserved for private use or Advance less than minimum"));
        CodeTable.put("O3", new CodeDetails("Reserved for private use or Delinquent"));
        CodeTable.put("O4", new CodeDetails("Reserved for private use or Over limit table"));
        CodeTable.put("O5", new CodeDetails("Reserved for private use or PIN required *"));
        CodeTable.put("O6", new CodeDetails("Reserved for private use or Mod 10 check"));
        CodeTable.put("O7", new CodeDetails("Reserved for private use or Force post"));
        CodeTable.put("O8", new CodeDetails("Reserved for private use or Bad PBF *"));
        CodeTable.put("O9", new CodeDetails("Reserved for private use or NEG file problem *"));
        CodeTable.put("P0", new CodeDetails("Reserved for private use or CAF problem *"));
        CodeTable.put("P1", new CodeDetails("Reserved for private use or Over daily limit *"));
        CodeTable.put("P2", new CodeDetails("Reserved for private use or CAPF not found *"));
        CodeTable.put("P3", new CodeDetails("Reserved for private use or Advance less than minimum"));
        CodeTable.put("P4", new CodeDetails("Reserved for private use or Number of times used"));
        CodeTable.put("P5", new CodeDetails("Reserved for private use or Delinquent"));
        CodeTable.put("P6", new CodeDetails("Reserved for private use or Over limit table"));
        CodeTable.put("P7", new CodeDetails("Reserved for private use or Advance less than minimum"));
        CodeTable.put("P8", new CodeDetails("Reserved for private use or Administrative card needed"));
        CodeTable.put("P9", new CodeDetails("Reserved for private use or Enter lesser amount"));
        CodeTable.put("Q0", new CodeDetails("Reserved for private use or Invalid transaction date"));
        CodeTable.put("Q1", new CodeDetails("Reserved for private use or Invalid expiration date"));
        CodeTable.put("Q2", new CodeDetails("Reserved for private use or Invalid transaction code"));
        CodeTable.put("Q3", new CodeDetails("Reserved for private use or Advance less than minimum"));
        CodeTable.put("Q4", new CodeDetails("Reserved for private use or Number of times used"));
        CodeTable.put("Q5", new CodeDetails("Reserved for private use or Delinquent"));
        CodeTable.put("Q6", new CodeDetails("Reserved for private use or Over limit table"));
        CodeTable.put("Q7", new CodeDetails("Reserved for private use or Amount over maximum"));
        CodeTable.put("Q8", new CodeDetails("Reserved for private use or Administrative card not found"));
        CodeTable.put("Q9", new CodeDetails("Reserved for private use or Administrative card not allowed"));
        CodeTable.put("R0", new CodeDetails("Reserved for private use or Approved administrative request performed in window *"));
        CodeTable.put("R1", new CodeDetails("Reserved for private use or Approved administrative request performed out of window *"));
        CodeTable.put("R2", new CodeDetails("Reserved for private use or Approved administrative request performed anytime *"));
        CodeTable.put("R3", new CodeDetails("Reserved for private use or Chargeback, customer file updated"));
        CodeTable.put("R4", new CodeDetails("Reserved for private use or Chargeback, customer file updated, acquirer not found"));
        CodeTable.put("R5", new CodeDetails("Reserved for private use or Chargeback, incorrect prefix number"));
        CodeTable.put("R6", new CodeDetails("Reserved for private use or Chargeback, incorrect response code or CPF configuration"));
        CodeTable.put("R7", new CodeDetails("Reserved for private use or Administrative transactions not supported"));
        CodeTable.put("R8", new CodeDetails("Reserved for private use or Card on national negative file *"));
        CodeTable.put("S4", new CodeDetails("PTLF full *"));
        CodeTable.put("S5", new CodeDetails("Reserved for private use or Chargeback approved, customer file not updated"));
        CodeTable.put("S6", new CodeDetails("Reserved for private use or Chargeback approved, customer file not updated, acquirer not found"));
        CodeTable.put("S7", new CodeDetails("Reserved for private use or Chargeback accepted, incorrect destination"));
        CodeTable.put("S8", new CodeDetails("Reserved for private use or ADMN file problem"));
        CodeTable.put("S9", new CodeDetails("Reserved for private use or Unable to validate PIN; security module is down *"));
        CodeTable.put("T1", new CodeDetails("Reserved for private use or Invalid credit card advance amount"));
        CodeTable.put("T2", new CodeDetails("Reserved for private use or Invalid transaction date"));
        CodeTable.put("T3", new CodeDetails("Reserved for private use or Card not supported"));
        CodeTable.put("T4", new CodeDetails("Reserved for private use or Amount over maximum"));
        CodeTable.put("T5", new CodeDetails("Reserved for private use or CAF status = 0 or 9 *"));
        CodeTable.put("T6", new CodeDetails("Reserved for private use or Bad UAF *"));
        CodeTable.put("T7", new CodeDetails("Reserved for private use or Cash back exceeds daily limit"));
        CodeTable.put("T8", new CodeDetails("Reserved for private use or Invalid account"));
        CodeTable.put("U0", new CodeDetails("ARQC failure decline *"));
        CodeTable.put("U1", new CodeDetails("Security module parameter error *"));
        CodeTable.put("U2", new CodeDetails("Security module failure *"));
        CodeTable.put("U3", new CodeDetails("KEY1 record not found *"));
        CodeTable.put("U4", new CodeDetails("ATC check failure *"));
        CodeTable.put("U5", new CodeDetails("CVR decline *"));
        CodeTable.put("U6", new CodeDetails("TVR decline *"));
        CodeTable.put("U7", new CodeDetails("Reason online code decline *"));
        CodeTable.put("U8", new CodeDetails("Fallback decline *"));
        CodeTable.put("V0", new CodeDetails("ARQC failure referral *"));
        CodeTable.put("V1", new CodeDetails("CVR referral *"));
        CodeTable.put("V2", new CodeDetails("TVR referral *"));
        CodeTable.put("V3", new CodeDetails("Reason online code referral *"));
        CodeTable.put("V4", new CodeDetails("Fallback referral *"));
        CodeTable.put("V7", new CodeDetails("ARQC failure capture *"));
        CodeTable.put("V8", new CodeDetails("CVR capture *"));
        CodeTable.put("V9", new CodeDetails("TVR capture *"));
        CodeTable.put("70", new CodeDetails("Error Descifrando Track2"));
        CodeTable.put("72", new CodeDetails("Error en activación manual"));
        CodeTable.put("73", new CodeDetails("Error en CRC"));

        CodeTable.put("000", new CodeDetails("Approved", true));
        CodeTable.put("001", new CodeDetails("Approve with ID", true));
        CodeTable.put("002", new CodeDetails("Partial Approval (Prepaid Cards only)"));
        CodeTable.put("100", new CodeDetails("Deny"));
        CodeTable.put("101", new CodeDetails("Expired Card / Invalid Expiration Date"));
        CodeTable.put("106", new CodeDetails("Exceeded PIN attempts"));
        CodeTable.put("109", new CodeDetails("Invalid merchant"));
        CodeTable.put("110", new CodeDetails("Invalid amount"));
        CodeTable.put("111", new CodeDetails("Invalid account / Invalid MICR (Travelers Cheque)"));
        CodeTable.put("115", new CodeDetails("Requested function not supported", true));
        CodeTable.put("116", new CodeDetails("Not Sufficient Funds", true));
        CodeTable.put("117", new CodeDetails("Invalid PIN"));
        CodeTable.put("119", new CodeDetails("Cardmember not enrolled / not permitted"));
        CodeTable.put("121", new CodeDetails("Limit Exceeded"));
        CodeTable.put("122", new CodeDetails("Invalid card security code (a.k.a., CID, 4DBC, 4CSC)"));
        CodeTable.put("125", new CodeDetails("Invalid effective date"));
        CodeTable.put("130", new CodeDetails("Additional customer identification required"));
        CodeTable.put("181", new CodeDetails("Format error"));
        CodeTable.put("183", new CodeDetails("Invalid currency code"));
        CodeTable.put("187", new CodeDetails("Deny - New card issued"));
        CodeTable.put("189", new CodeDetails("Deny - Canceled or Closed Merchant/SE"));
        CodeTable.put("190", new CodeDetails("National ID Mismatch"));
        CodeTable.put("193", new CodeDetails("Invalid Country Code"));
        CodeTable.put("200", new CodeDetails("Deny - Pick up card"));
        CodeTable.put("900", new CodeDetails("Accepted - ATC Synchronization"));
        CodeTable.put("909", new CodeDetails("System Malfunction (Cryptographic error)"));
        CodeTable.put("912", new CodeDetails("Issuer not available"));
        CodeTable.put("977", new CodeDetails("Invalid Payment Plan"));
        CodeTable.put("978", new CodeDetails("Invalid Payment Times"));
        CodeTable.put("400",new CodeDetails("Transaction not processed, reverse generated"));
    }

    public static HashMap<String, CodeDetails> getCodeResponses() { return CodeTable; }

    public static CodeDetails getCodeDetails(String key) {
        CodeDetails details = CodeTable.get(key);
        return details != null ? details : new CodeDetails("Error desconocido");
    }

    public static class CodeDetails {
        public String description;
        public boolean isSuccessCode;

        public CodeDetails(String _description,boolean... _isSuccessCode) {
            this.description = _description;
            this.isSuccessCode = _isSuccessCode.length > 0 ? _isSuccessCode[0] : false;
        }

    }

}
