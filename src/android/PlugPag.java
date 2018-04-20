package com.fjti.plugpag;

import android.app.Activity;
import android.content.Context;
import android.telecom.Call;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.uol.pagseguro.plugpag.DeviceInfo;
import br.com.uol.pagseguro.plugpag.PlugPagAppIdentification;
import br.com.uol.pagseguro.plugpag.PlugPagDevice;
import br.com.uol.pagseguro.plugpag.PlugPagPaymentData;
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult;


/**
 * This class echoes a string called from JavaScript.
 */
public class PlugPag extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }

        if(action.equals("getDeviceInfos")){
            this.InitDeviceInfo(callbackContext);
            return true;
        }

        if(action.equals("getLibVersion")){
          this.GetLibVersion(callbackContext);
          return true;
        }

        if(action.equals("checkAuthentication")){
          this.CheckAuthentication(callbackContext);
          return true;
        }

        if(action.equals("invalidateAuthentication")){
          this.InvalidateAuthentication(callbackContext);
          return true;
        }

        if(action.equals("showAuthenticationActivity")){
          this.ShowAuthenticationActivity(callbackContext);
          return true;
        }

        if(action.equals("startPayment")){
          JSONObject paymentInfo =  args.getJSONObject(0);

          this.startPayment(paymentInfo.getString("deviceIdentification"),
            paymentInfo.getInt("PaymentType"),
            paymentInfo.getInt("InstallmentType"),
            paymentInfo.getString("SaleRef"),
            paymentInfo.getInt("installments"),
            paymentInfo.getInt("amount") , callbackContext);

          return true;
        }


        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
          callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

  private void InitDeviceInfo(final CallbackContext callbackContext) {
    Context context = this.cordova.getActivity().getApplicationContext();
    DeviceInfo deviceInfo = new DeviceInfo(context);
    String deviceInformations = "DeviceId: " + deviceInfo.getDeviceId() +"\nDeviceModel: "
                                + deviceInfo.getDeviceModel() + "\nDeviceImei: "
                                + deviceInfo.getImei() +"\nDeviceOS: "
                                + deviceInfo.getOs() +"\nDeviceOSversion: "
                                + deviceInfo.getOsVersion();

    callbackContext.success(deviceInformations);
  }

  private void GetLibVersion(final CallbackContext callbackContext){
      Context context = this.cordova.getActivity().getApplicationContext();
    // Cria a identificação do aplicativo
      PlugPagAppIdentification appIdentification = new PlugPagAppIdentification("Quero2Business","0.0.1");

    // Cria a referência do PlugPag
        br.com.uol.pagseguro.plugpag.PlugPag plugpag = new br.com.uol.pagseguro.plugpag.PlugPag(context,appIdentification);
    // Obtém a versão da biblioteca
        String version = plugpag.getLibVersion();
    callbackContext.success(version);

  }

  public void CheckAuthentication(final CallbackContext callbackContext) throws JSONException {
    Context context = this.cordova.getActivity().getApplicationContext();
    // Cria a identificação do aplicativo
    PlugPagAppIdentification appIdentification = new PlugPagAppIdentification("Quero2Business","0.0.1");

    // Cria a referência do PlugPag
    br.com.uol.pagseguro.plugpag.PlugPag plugpag = new br.com.uol.pagseguro.plugpag.PlugPag(context,appIdentification);
    // Verifica autenticação
      boolean authenticated = plugpag.isAuthenticated();
      JSONObject json = new JSONObject();
      json.put("isAuthenticated", authenticated);
      callbackContext.success(json);
  }

  public void InvalidateAuthentication(final CallbackContext callbackContext) {
    Context context = this.cordova.getActivity().getApplicationContext();
    // Cria a identificação do aplicativo
    PlugPagAppIdentification appIdentification = new PlugPagAppIdentification("Quero2Business","0.0.1");

    // Cria a referência do PlugPag
    br.com.uol.pagseguro.plugpag.PlugPag plugpag = new br.com.uol.pagseguro.plugpag.PlugPag(context,appIdentification);
    try{
      // Invalida a autenticação existente
      plugpag.invalidateAuthentication();
      callbackContext.success();
    }catch (Exception ex){
      callbackContext.success(ex.getMessage());
    }
  }

  public void ShowAuthenticationActivity(final CallbackContext callbackContext) {
      Context context = this.cordova.getActivity().getApplicationContext();
    Activity activity = this.cordova.getActivity();

    // Cria a identificação do aplicativo
    PlugPagAppIdentification appIdentification = new PlugPagAppIdentification("Quero2Business","0.0.1");

    // Cria a referência do PlugPag
    br.com.uol.pagseguro.plugpag.PlugPag plugpag = new br.com.uol.pagseguro.plugpag.PlugPag(context,appIdentification);
    try{
      // Solicita autenticação
      plugpag.requestAuthentication(activity);
      callbackContext.success();
    }catch (Exception ex){
      callbackContext.error(ex.getMessage());
    }
  }

  /**
   * @param deviceIdentification Nome ou MAC address do leitor/pinpad
   * @param PaymentType Credito 1, Debito 2, Voucher 3
   * @param InstallmentType A_VISTA 1, PARC_VENDEDOR 2
   * */
  public void startPayment(String deviceIdentification, int PaymentType, int InstallmentType, String SaleRef, int installments, int amount , final CallbackContext callbackContext) throws JSONException {
    Context context = this.cordova.getActivity().getApplicationContext();
    // Define o terminal ou leitor que será utilizado para transação
    PlugPagDevice device = new PlugPagDevice(deviceIdentification);
        // Define os dados do pagamento
        br.com.uol.pagseguro.plugpag.PlugPagPaymentData paymentData =
          new PlugPagPaymentData(
            PaymentType,
            amount,
            InstallmentType,
            installments,
            "CODVENDA");
    // Cria a identificação do aplicativo
    PlugPagAppIdentification appIdentification = new PlugPagAppIdentification("Quero2Business","0.0.1");
    // Cria a referência do PlugPag
        br.com.uol.pagseguro.plugpag.PlugPag plugpag = new br.com.uol.pagseguro.plugpag.PlugPag(context, appIdentification);
        // Prepara conexão bluetooth e faz o pagamento
        PlugPagTransactionResult initResult = plugpag.initBTConnection(device);
        if (initResult.getResult() == br.com.uol.pagseguro.plugpag.PlugPag.RET_OK) {
          PlugPagTransactionResult result = plugpag.doPayment(paymentData);

          JSONObject transactionResult = this.GetTransactionResultStr(result);

          callbackContext.success(transactionResult);
        }
  }

  private JSONObject GetTransactionResultStr(PlugPagTransactionResult result) throws JSONException {
    JSONObject resultJson = new JSONObject();
    resultJson.put("Amount",result.getAmount());
    resultJson.put("AvailableBalance",result.getAvailableBalance());
    resultJson.put("Bin",result.getBin());
    resultJson.put("CardBrand",result.getCardBrand());
    resultJson.put("Date",result.getDate());
    resultJson.put("ErrorCode",result.getErrorCode());
    resultJson.put("Message",result.getMessage());
    resultJson.put("TransactionCode",result.getTransactionCode());
    resultJson.put("TransactionId",result.getTransactionId());
    resultJson.put("UserReference",result.getUserReference());

    return resultJson;
  }
}
