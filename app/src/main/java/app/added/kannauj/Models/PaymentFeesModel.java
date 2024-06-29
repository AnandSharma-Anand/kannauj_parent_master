package app.added.kannauj.Models;

import java.io.Serializable;
import java.util.List;

public class PaymentFeesModel implements Serializable {

    /**
     * error : 0
     * error_code : 0
     * message :
     * data : {"fee_details":{"Month":[{"MonthName":"April","FeePriority":"10","FeeHeadDetails":[{"FeeHead":"Dev charge + other charges","StudentFeeStructureID":"38067","AmountPayable":"1880","AmountPaid":"","DueAmount":"1880"},{"FeeHead":"tuition fee","StudentFeeStructureID":"38072","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"May","FeePriority":"20","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38073","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"June","FeePriority":"30","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38074","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38068","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"July","FeePriority":"40","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38075","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"August","FeePriority":"50","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38076","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38069","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"September","FeePriority":"60","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38077","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"October","FeePriority":"70","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38078","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"November","FeePriority":"80","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38079","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38070","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"December","FeePriority":"90","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38080","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"January","FeePriority":"100","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38081","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"February","FeePriority":"110","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38082","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38071","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"March","FeePriority":"120","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38083","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]}]},"FeeSubmissionLastMonthPriority":"10"}
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
        /**
         * fee_details : {"Month":[{"MonthName":"April","FeePriority":"10","FeeHeadDetails":[{"FeeHead":"Dev charge + other charges","StudentFeeStructureID":"38067","AmountPayable":"1880","AmountPaid":"","DueAmount":"1880"},{"FeeHead":"tuition fee","StudentFeeStructureID":"38072","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"May","FeePriority":"20","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38073","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"June","FeePriority":"30","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38074","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38068","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"July","FeePriority":"40","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38075","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"August","FeePriority":"50","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38076","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38069","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"September","FeePriority":"60","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38077","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"October","FeePriority":"70","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38078","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"November","FeePriority":"80","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38079","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38070","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"December","FeePriority":"90","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38080","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"January","FeePriority":"100","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38081","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]},{"MonthName":"February","FeePriority":"110","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38082","AmountPayable":"820","AmountPaid":"","DueAmount":"820"},{"FeeHead":"Examination fee","StudentFeeStructureID":"38071","AmountPayable":"150","AmountPaid":"","DueAmount":"150"}]},{"MonthName":"March","FeePriority":"120","FeeHeadDetails":[{"FeeHead":"tuition fee","StudentFeeStructureID":"38083","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]}]}
         * FeeSubmissionLastMonthPriority : 10
         */

        private FeeDetailsBean fee_details;
        private String FeeSubmissionLastMonthPriority;

        public FeeDetailsBean getFee_details() {
            return fee_details;
        }

        public void setFee_details(FeeDetailsBean fee_details) {
            this.fee_details = fee_details;
        }

        public String getFeeSubmissionLastMonthPriority() {
            return FeeSubmissionLastMonthPriority;
        }

        public void setFeeSubmissionLastMonthPriority(String FeeSubmissionLastMonthPriority) {
            this.FeeSubmissionLastMonthPriority = FeeSubmissionLastMonthPriority;
        }

        public static class FeeDetailsBean {
            private List<MonthBean> Month;

            public List<MonthBean> getMonth() {
                return Month;
            }

            public void setMonth(List<MonthBean> Month) {
                this.Month = Month;
            }

            public static class MonthBean {
                /**
                 * MonthName : April
                 * FeePriority : 10
                 * FeeHeadDetails : [{"FeeHead":"Dev charge + other charges","StudentFeeStructureID":"38067","AmountPayable":"1880","AmountPaid":"","DueAmount":"1880"},{"FeeHead":"tuition fee","StudentFeeStructureID":"38072","AmountPayable":"820","AmountPaid":"","DueAmount":"820"}]
                 */

                private String MonthName;
                private String FeePriority;
                private List<FeeHeadDetailsBean> FeeHeadDetails;

                public String getMonthName() {
                    return MonthName;
                }

                public void setMonthName(String MonthName) {
                    this.MonthName = MonthName;
                }

                public String getFeePriority() {
                    return FeePriority;
                }

                public void setFeePriority(String FeePriority) {
                    this.FeePriority = FeePriority;
                }

                public List<FeeHeadDetailsBean> getFeeHeadDetails() {
                    return FeeHeadDetails;
                }

                public void setFeeHeadDetails(List<FeeHeadDetailsBean> FeeHeadDetails) {
                    this.FeeHeadDetails = FeeHeadDetails;
                }

                public static class FeeHeadDetailsBean {
                    /**
                     * FeeHead : Dev charge + other charges
                     * StudentFeeStructureID : 38067
                     * AmountPayable : 1880
                     * AmountPaid :
                     * DueAmount : 1880
                     */

                    private String FeeHead;
                    private String StudentFeeStructureID;
                    private String AmountPayable;
                    private String AmountPaid;
                    private String DueAmount;

                    public String getFeeHead() {
                        return FeeHead;
                    }

                    public void setFeeHead(String FeeHead) {
                        this.FeeHead = FeeHead;
                    }

                    public String getStudentFeeStructureID() {
                        return StudentFeeStructureID;
                    }

                    public void setStudentFeeStructureID(String StudentFeeStructureID) {
                        this.StudentFeeStructureID = StudentFeeStructureID;
                    }

                    public String getAmountPayable() {
                        return AmountPayable;
                    }

                    public void setAmountPayable(String AmountPayable) {
                        this.AmountPayable = AmountPayable;
                    }

                    public String getAmountPaid() {
                        return AmountPaid;
                    }

                    public void setAmountPaid(String AmountPaid) {
                        this.AmountPaid = AmountPaid;
                    }

                    public String getDueAmount() {
                        return DueAmount;
                    }

                    public void setDueAmount(String DueAmount) {
                        this.DueAmount = DueAmount;
                    }
                }
            }
        }
    }
}
