package app.added.kannauj.Models;

import java.util.List;

public class FeesReceiptModel {

    /**
     * error : 0
     * error_code : 0
     * message :
     * data : {"FeeReceipts":[{"Header":"Receipt [ 02/05/2019 ]","FeeTransactionID":1580,"FeeReceiptPage":"https://demo.addedschools.com/admin/fee_management/fee_receipt.php?FeeCollectionID=1580&is_temp=1"},{"Header":"Receipt [ 02/05/2019 ]","FeeTransactionID":1582,"FeeReceiptPage":"https://demo.addedschools.com/admin/fee_management/fee_receipt.php?FeeCollectionID=1582&is_temp=1"},{"Header":"Receipt [ 02/12/2019 ]","FeeTransactionID":2046,"FeeReceiptPage":"https://demo.addedschools.com/admin/fee_management/fee_receipt.php?FeeCollectionID=2046&is_temp=1"}]}
     */

    private int error;
    private int error_code;
    private String message;
    private DataBean data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<FeeReceiptsBean> FeeReceipts;

        public List<FeeReceiptsBean> getFeeReceipts() {
            return FeeReceipts;
        }

        public void setFeeReceipts(List<FeeReceiptsBean> FeeReceipts) {
            this.FeeReceipts = FeeReceipts;
        }

        public static class FeeReceiptsBean {
            /**
             * Header : Receipt [ 02/05/2019 ]
             * FeeTransactionID : 1580
             * FeeReceiptPage : https://demo.addedschools.com/admin/fee_management/fee_receipt.php?FeeCollectionID=1580&is_temp=1
             */

            private String Header;
            private int FeeTransactionID;
            private String FeeReceiptPage;

            public String getHeader() {
                return Header;
            }

            public void setHeader(String Header) {
                this.Header = Header;
            }

            public int getFeeTransactionID() {
                return FeeTransactionID;
            }

            public void setFeeTransactionID(int FeeTransactionID) {
                this.FeeTransactionID = FeeTransactionID;
            }

            public String getFeeReceiptPage() {
                return FeeReceiptPage;
            }

            public void setFeeReceiptPage(String FeeReceiptPage) {
                this.FeeReceiptPage = FeeReceiptPage;
            }
        }
    }
}
