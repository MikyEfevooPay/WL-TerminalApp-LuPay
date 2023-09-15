package com.efevoopay.demoui.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

import com.dspread.xpos.QPOSService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QPOSStatus implements QPOSService.QPOSServiceListener {
    private static QPOSStatus instance;
    private  HashMap<String, QPOSService.QPOSServiceListener> listeners;

    public QPOSStatus() {
        this.listeners = new HashMap();
    }

    public static synchronized QPOSStatus getInstance() {
        if(instance == null) {
            instance = new QPOSStatus();
        }
        return instance;
    }

    @SuppressLint("NewApi")
    public void addActivityListeners(String key, QPOSService.QPOSServiceListener listener) {
        this.listeners.remove(key);
        this.listeners.put(key, listener);
    }


    @Override
    public void onGetDeviceTestResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetDeviceTestResult(b);
        }
    }

    @Override
    public void onReturnCheckCardResult(boolean b, Hashtable<String, String> data) {
        TRACE.d("onReturnReadCardResult()");
    }

    @Override
    public void onReturnReadCardResult(boolean b, Hashtable<String, String> data) {
        TRACE.d("onReturnReadCardResult()");
    }

    @Override
    public void onReturnSearchCardResult(boolean b, Hashtable<String, String> data) {
        TRACE.d("onReturnSearchCardResult()");
    }

    @Override
    public void onReturnPowerOffCardResult(boolean b, Hashtable<String, String> data) {
        TRACE.d("onReturnPowerOffCardResult()");
    }

    @Override
    public void onReturnPowerOnCardResult(boolean b, Hashtable<String, String> data) {
        TRACE.d("onReturnPowerOnCardResult()");
    }

    @Override
    public void onQposRequestPinResult(List<String> list, int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposRequestPinResult(list, i);
        }
    }

    @Override
    public void onReturnD20SleepTimeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnD20SleepTimeResult(b);
        }
    }

    @Override
    public void onQposRequestPinStartResult(List<String> list) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposRequestPinStartResult(list);
        }
    }

    @Override
    public void onQposPinMapSyncResult(boolean b, boolean b1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposPinMapSyncResult(b,b1);
        }
    }

    @Override
    public void onRequestWaitingUser() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestWaitingUser();
        }
    }

    @Override
    public void onReturnSyncVersionInfo(QPOSService.FirmwareStatus firmwareStatus, String s, QPOSService.QposStatus qposStatus) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSyncVersionInfo(firmwareStatus, s, qposStatus);
        }
    }

    @Override
    public void onReturnSpLogResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSpLogResult(s);
        }
    }

    @Override
    public void onReturnRsaResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnRsaResult(s);
        }
    }

    @Override
    public void onQposInitModeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposInitModeResult(b);
        }
    }

    @Override
    public void onD20StatusResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onD20StatusResult(s);
        }
    }

    @Override
    public void onQposTestSelfCommandResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposTestSelfCommandResult(b,s);
        }
    }

    @Override
    public void onQposTestCommandResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposTestCommandResult(b,s);
        }
    }

    @Override
    public void onQposGetRealTimeSelfDestructStatus(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposGetRealTimeSelfDestructStatus(b,s);
        }
    }

    @Override
    public void onReturPosSelfDestructRecords(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturPosSelfDestructRecords(b,s);
        }
    }

    @Override
    public void onQposIdResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposIdResult(hashtable);
        }
    }

    @Override
    public void onQposKsnResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposKsnResult(hashtable);
        }
    }

    @Override
    public void onQposIsCardExist(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposIsCardExist(b);
        }
    }

    @Override
    public void onRequestDeviceScanFinished() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestDeviceScanFinished();
        }
    }

    @Override
    public void onQposInfoResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposInfoResult(hashtable);
        }
    }

    @Override
    public void onQposTestResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposTestResult(hashtable);
        }
    }

    @Override
    public void onQposCertificateInfoResult(List<String> list) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposCertificateInfoResult(list);
        }
    }

    @Override
    public void onQposGenerateSessionKeysResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposGenerateSessionKeysResult(hashtable);
        }
    }

    @Override
    public void onQposDoSetRsaPublicKey(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposDoSetRsaPublicKey(b);
        }
    }

    @Override
    public void onSearchMifareCardResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSearchMifareCardResult(hashtable);
        }
    }

    @Override
    public void onBatchReadMifareCardResult(String s, Hashtable<String, List<String>> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBatchReadMifareCardResult(s, hashtable);
        }
    }

    @Override
    public void onBatchWriteMifareCardResult(String s, Hashtable<String, List<String>> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBatchWriteMifareCardResult(s, hashtable);
        }
    }

    @Override
    public void onDoTradeResult(QPOSService.DoTradeResult onDoTradeResult, Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onDoTradeResult(onDoTradeResult, hashtable);
        }
    }

    @Override
    public void onFinishMifareCardResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onFinishMifareCardResult(b);
        }
    }

    @Override
    public void onVerifyMifareCardResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onVerifyMifareCardResult(b);
        }
    }

    @Override
    public void onReadMifareCardResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReadMifareCardResult(hashtable);
        }
    }

    @Override
    public void onWriteMifareCardResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onWriteMifareCardResult(b);
        }
    }

    @Override
    public void onOperateMifareCardResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onOperateMifareCardResult(hashtable);
        }
    }

    @Override
    public void getMifareCardVersion(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().getMifareCardVersion(hashtable);
        }
    }

    @Override
    public void getMifareReadData(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().getMifareReadData(hashtable);
        }
    }

    @Override
    public void getMifareFastReadData(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().getMifareFastReadData(hashtable);
        }
    }

    @Override
    public void writeMifareULData(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().writeMifareULData(s);
        }
    }

    @Override
    public void verifyMifareULData(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().verifyMifareULData(hashtable);
        }
    }

    @Override
    public void transferMifareData(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().transferMifareData(s);
        }
    }

    @Override
    public void onRequestSetAmount() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestSetAmount();
        }
    }

    @Override
    public void onRequestSelectEmvApp(ArrayList<String> arrayList) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestSelectEmvApp(arrayList);
        }
    }

    @Override
    public void onRequestIsServerConnected() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestIsServerConnected();
        }
    }

    @Override
    public void onRequestFinalConfirm() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestFinalConfirm();
        }
    }

    @Override
    public void onRequestOnlineProcess(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestOnlineProcess(s);
        }
    }

    @Override
    public void onRequestTime() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestTime();
        }
    }

    @Override
    public void onRequestTransactionResult(QPOSService.TransactionResult transactionResult) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestTransactionResult(transactionResult);
        }
    }

    @Override
    public void onRequestTransactionLog(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestTransactionLog(s);
        }
    }

    @Override
    public void onRequestBatchData(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestBatchData(s);
        }
    }

    @Override
    public void onRequestQposConnected() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestQposConnected();
        }
    }

    @Override
    public void onRequestQposDisconnected() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestQposDisconnected();
        }
    }

    @Override
    public void onRequestNoQposDetected() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestNoQposDetected();
        }
    }

    @Override
    public void onRequestNoQposDetectedUnbond() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestNoQposDetectedUnbond();
        }
    }

    @Override
    public void onError(QPOSService.Error error) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onError(error);
        }
    }

    @Override
    public void onRequestDisplay(QPOSService.Display display) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestDisplay(display);
        }
    }

    @Override
    public void onReturnReversalData(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnReversalData(s);
        }
    }

    @Override
    public void onReturnGetPinInputResult(int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetPinInputResult(i);
        }
    }

    @Override
    public void onReturnGetKeyBoardInputResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetKeyBoardInputResult(s);
        }
    }

    @Override
    public void onReturnGetPinResult(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetPinResult(hashtable);
        }
    }

    @Override
    public void onReturnPowerOnIccResult(boolean b, String s, String s1, int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPowerOnIccResult(b, s, s1, i);
        }
    }

    @Override
    public void onReturnPowerOffIccResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPowerOffIccResult(b);
        }
    }

    @Override
    public void onReturnApduResult(boolean b, String s, int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnApduResult(b, s, i);
        }
    }

    @Override
    public void onReturnPowerOnFelicaResult(QPOSService.FelicaStatusCode felicaStatusCode) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPowerOnFelicaResult(felicaStatusCode);
        }
    }

    @Override
    public void onReturnPowerOffFelicaResult(QPOSService.FelicaStatusCode felicaStatusCode) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPowerOffFelicaResult(felicaStatusCode);
        }
    }

    @Override
    public void onReturnSendApduFelicaResult(QPOSService.FelicaStatusCode felicaStatusCode, String s, String s1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSendApduFelicaResult(felicaStatusCode, s, s1);
        }
    }

    @Override
    public void onReturnSetSleepTimeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSetSleepTimeResult(b);
        }
    }

    @Override
    public void onGetCardNoResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetCardNoResult(s);
        }
    }

    @Override
    public void onRequestSignatureResult(byte[] bytes) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestSignatureResult(bytes);
        }
    }

    @Override
    public void onRequestCalculateMac(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestCalculateMac(s);
        }
    }

    @Override
    public void onRequestUpdateWorkKeyResult(QPOSService.UpdateInformationResult updateInformationResult) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestUpdateWorkKeyResult(updateInformationResult);
        }
    }

    @Override
    public void onRequestSendTR31KeyResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestSendTR31KeyResult(b);
        }
    }

    @Override
    public void onReturnCustomConfigResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnCustomConfigResult(b, s);
        }
    }

    @Override
    public void onReturnDoInputCustomStr(boolean b, String s, String s1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnDoInputCustomStr(b, s, s1);
        }
    }

    @Override
    public void onRetuenGetTR31Token(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRetuenGetTR31Token(s);
        }
    }

    @Override
    public void onRequestSetPin() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestSetPin();
        }
    }

    @Override
    public void onReturnSetMasterKeyResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSetMasterKeyResult(b);
        }
    }

    @Override
    public void onRequestUpdateKey(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestUpdateKey(s);
        }
    }

    @Override
    public void onReturnUpdateIPEKResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnUpdateIPEKResult(b);
        }
    }

    @Override
    public void onReturnRSAResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnRSAResult(s);
        }
    }

    @Override
    public void onReturnUpdateEMVResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnUpdateEMVResult(b);
        }
    }

    @Override
    public void onReturnGetQuickEmvResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetQuickEmvResult(b);
        }
    }

    @Override
    public void onReturnGetEMVListResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetEMVListResult(s);
        }
    }

    @Override
    public void onReturnGetCustomEMVListResult(Map<String, String> map) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetCustomEMVListResult(map);
        }
    }

    @Override
    public void onReturnUpdateEMVRIDResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnUpdateEMVRIDResult(b);
        }
    }

    @Override
    public void onDeviceFound(BluetoothDevice bluetoothDevice) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onDeviceFound(bluetoothDevice);
        }
    }

    @Override
    public void onReturnBatchSendAPDUResult(LinkedHashMap<Integer, String> linkedHashMap) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnBatchSendAPDUResult(linkedHashMap);
        }
    }

    @Override
    public void onBluetoothBonding() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBluetoothBonding();
        }
    }

    @Override
    public void onBluetoothBonded() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBluetoothBonded();
        }
    }

    @Override
    public void onWaitingforData(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onWaitingforData(s);
        }
    }

    @Override
    public void onBluetoothBondFailed() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBluetoothBondFailed();
        }
    }

    @Override
    public void onBluetoothBondTimeout() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBluetoothBondTimeout();
        }
    }

    @Override
    public void onReturniccCashBack(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturniccCashBack(hashtable);
        }
    }

    @Override
    public void onLcdShowCustomDisplay(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onLcdShowCustomDisplay(b);
        }
    }

    @Override
    public void onSetCustomLogoDisplay(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetCustomLogoDisplay(b);
        }
    }

    @Override
    public void onUpdatePosFirmwareResult(QPOSService.UpdateInformationResult updateInformationResult) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onUpdatePosFirmwareResult(updateInformationResult);
        }
    }

    @Override
    public void onReturnPosFirmwareUpdateProgressResult(int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPosFirmwareUpdateProgressResult(i);
        }
    }

    @Override
    public void onBluetoothBoardStateResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onBluetoothBoardStateResult(b);
        }
    }

    @Override
    public void onReturnDownloadRsaPublicKey(HashMap<String, String> hashMap) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnDownloadRsaPublicKey(hashMap);
        }
    }

    @Override
    public void onGetPosComm(int i, String s, String s1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetPosComm(i, s, s1);
        }
    }

    @Override
    public void onUpdateMasterKeyResult(boolean b, Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onUpdateMasterKeyResult(b, hashtable);
        }
    }

    @Override
    public void onPinKey_TDES_Result(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onPinKey_TDES_Result(s);
        }
    }

    @Override
    public void onEmvICCExceptionData(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onEmvICCExceptionData(s);
        }
    }

    @Override
    public void onSetParamsResult(boolean b, Hashtable<String, Object> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetParamsResult(b, hashtable);
        }
    }

    @Override
    public void onSetVendorIDResult(boolean b, Hashtable<String, Object> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetVendorIDResult(b, hashtable);
        }
    }

    @Override
    public void onGetInputAmountResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetInputAmountResult(b, s);
        }
    }

    @Override
    public void onReturnNFCApduResult(boolean b, String s, int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnNFCApduResult(b, s, i);
        }
    }

    @Override
    public void onReturnMPUCardInfo(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnMPUCardInfo(hashtable);
        }
    }

    @Override
    public void onReturnPowerOnNFCResult(boolean b, String s, String s1, int i) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPowerOnNFCResult(b, s, s1, i);
        }
    }

    @Override
    public void onReturnPowerOffNFCResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPowerOffNFCResult(b);
        }
    }

    @Override
    public void onCbcMacResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onCbcMacResult(s);
        }
    }

    @Override
    public void onReadBusinessCardResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReadBusinessCardResult(b, s);
        }
    }

    @Override
    public void onReadGasCardResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReadGasCardResult(b, s);
        }
    }

    @Override
    public void onWriteBusinessCardResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onWriteBusinessCardResult(b);
        }
    }

    @Override
    public void onWriteGasCardResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onWriteGasCardResult(b);
        }
    }

    @Override
    public void onConfirmAmountResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onConfirmAmountResult(b);
        }
    }

    @Override
    public void onSetManagementKey(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetManagementKey(b);
        }
    }

    @Override
    public void onSetSleepModeTime(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetSleepModeTime(b);
        }
    }

    @Override
    public void onGetSleepModeTime(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetSleepModeTime(s);
        }
    }

    @Override
    public void onGetShutDownTime(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetShutDownTime(s);
        }
    }

    @Override
    public void onEncryptData(Hashtable<String, String> hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onEncryptData(hashtable);
        }
    }

    @Override
    public void onAddKey(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onAddKey(b);
        }
    }

    @Override
    public void onSetBuzzerResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetBuzzerResult(b);
        }
    }

    @Override
    public void onSetBuzzerTimeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetBuzzerTimeResult(b);
        }
    }

    @Override
    public void onSetBuzzerStatusResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetBuzzerStatusResult(b);
        }
    }

    @Override
    public void onGetBuzzerStatusResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetBuzzerStatusResult(s);
        }
    }

    @Override
    public void onReturnPlayBuzzerByTypeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnPlayBuzzerByTypeResult(b);
        }
    }

    @Override
    public void onReturnOperateLEDByTypeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnOperateLEDByTypeResult(b);
        }
    }

    @Override
    public void onQposDoTradeLog(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposDoTradeLog(b);
        }
    }

    @Override
    public void onQposDoGetTradeLogNum(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposDoGetTradeLogNum(s);
        }
    }

    @Override
    public void onQposDoGetTradeLog(String s, String s1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposDoGetTradeLog(s, s1);
        }
    }

    @Override
    public void onRequestDevice() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestDevice();
        }
    }

    @Override
    public void onGetKeyCheckValue(List<String> list) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetKeyCheckValue(list);
        }
    }

    @Override
    public void onGetDevicePubKey(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onGetDevicePubKey(s);
        }
    }

    @Override
    public void onSetPosBluConfig(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onSetPosBluConfig(b);
        }
    }

    @Override
    public void onTradeCancelled() {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onTradeCancelled();
        }
    }

    @Override
    public void onReturnSetAESResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSetAESResult(b, s);
        }
    }

    @Override
    public void onReturnAESTransmissonKeyResult(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnAESTransmissonKeyResult(b, s);
        }
    }

    @Override
    public void onReturnSignature(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSignature(b, s);
        }
    }

    @Override
    public void onReturnConverEncryptedBlockFormat(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnConverEncryptedBlockFormat(s);
        }
    }

    @Override
    public void onQposIsCardExistInOnlineProcess(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposIsCardExistInOnlineProcess(b);
        }
    }

    @Override
    public void onReturnSetConnectedShutDownTimeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSetConnectedShutDownTimeResult(b);
        }
    }

    @Override
    public void onReturnGetConnectedShutDownTimeResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnGetConnectedShutDownTimeResult(s);
        }
    }

    @Override
    public void onRequestNFCBatchData(QPOSService.TransactionResult transactionResult, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestNFCBatchData(transactionResult, s);
        }
    }

    @Override
    public void onReturnupdateKeyByTR_31Result(boolean b, String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnupdateKeyByTR_31Result(b, s);
        }
    }

    @Override
    public void onRequestGenerateTransportKey(Hashtable hashtable) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onRequestGenerateTransportKey(hashtable);
        }
    }

    @Override
    public void onReturnAnalyseDigEnvelop(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnAnalyseDigEnvelop(s);
        }
    }

    @Override
    public void onReturnDisplayQRCodeResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnDisplayQRCodeResult(b);
        }
    }

    @Override
    public void onReturnDeviceCSRResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnDeviceCSRResult(s);
        }
    }

    @Override
    public void onReturnStoreCertificatesResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnStoreCertificatesResult(b);
        }
    }

    @Override
    public void onReturnSignatureAndCertificatesResult(String s, String s1, String s2) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnSignatureAndCertificatesResult(s, s1, s2);
        }
    }

    @Override
    public void onReturnDeviceSigningCertResult(String s, String s1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnDeviceSigningCertResult(s, s1);
        }
    }

    @Override
    public void onReturnServerCertResult(String s, String s1) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onReturnServerCertResult(s, s1);
        }
    }

    @Override
    public void onQposSetLEDColorResult(boolean b) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposSetLEDColorResult(b);
        }
    }

    @Override
    public void onQposGetLEDColorResult(String s) {
        for (Map.Entry<String, QPOSService.QPOSServiceListener> entry : this.listeners.entrySet()) {
            entry.getValue().onQposGetLEDColorResult(s);
        }
    }
}
