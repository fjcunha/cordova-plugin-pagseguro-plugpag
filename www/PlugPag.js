var exec = require('cordova/exec');


var methods = {
    getDeviceInfos:function(success, error) {
        // console.log('GETTING DEVICE INFOS');
        exec(success, error, 'PlugPag', 'getDeviceInfos', []);
    },
    getLibVersion:function(success,error){
        // console.log('GETTING LIB Version');
        exec(success, error, 'PlugPag', 'getLibVersion', []);
    },
    checkAuthentication:function(success,error){
        // console.log('Checking authentication');
        exec(success, error, 'PlugPag', 'checkAuthentication', []);
    },
    invalidateAuthentication:function(success,error){
        // console.log('Invalidating authentication');
        exec(success, error, 'PlugPag', 'invalidateAuthentication', []);
    },
    showAuthenticationActivity:function(success,error){
        // console.log('show authentication activity');
        exec(success, error, 'PlugPag', 'showAuthenticationActivity', []);
    },
    /**
     * @argument paymentInfo  {
     *  deviceIdentification:string,
     *  PaymentType:int,
     *  SaleRef:string,
     *  InstallmentType:int,
     *  installments:int,
     *  amount:int
     * }
     * */
    startPayment:function(paymentInfo,success,error){
        // console.log('show authentication activity');
        exec(success, error, 'PlugPag', 'startPayment', [deviceIdentification]);
    }
}

module.exports = methods;
