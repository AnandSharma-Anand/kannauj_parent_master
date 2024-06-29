package app.added.kannauj.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import app.added.kannauj.Models.PaymentFeeGroupModel;
import app.added.kannauj.Models.PaymentFeeModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.CustomTextView;

public class AdvancedFeePaymentAdapter extends ExpandableRecyclerViewAdapter<AdvancedFeePaymentAdapter.MonthViewHolder, AdvancedFeePaymentAdapter.FeeViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<? extends ExpandableGroup> groups;
    onClickLitener onClickLitener;
    //  private ExpandCollapseController expandCollapseController;
    int dueAmount = 0;

    // private OnGroupClickListener groupClickListener;
    //  private GroupExpandCollapseListener expandCollapseListener;
    public AdvancedFeePaymentAdapter(List<? extends ExpandableGroup> groups, Context context, onClickLitener onClickLitener) {
        super(groups);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.groups = groups;
        this.onClickLitener = onClickLitener;

        //   this.expandCollapseController = new ExpandCollapseController(expandableList, this);
    }

   /* @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(position);
        ExpandableGroup group = expandableList.getExpandableGroup(listPos);
        switch (listPos.type) {
            case ExpandableListPosition.GROUP:
                onBindGroupViewHolder((MonthViewHolder) holder, position, group);

                if (isGroupExpanded(group)) {
                    ((MonthViewHolder) holder).expand();
                } else {
                    ((MonthViewHolder) holder).collapse();
                }
                break;
            case ExpandableListPosition.CHILD:
                onBindChildViewHolder((FeeViewHolder) holder, position, group, listPos.childPos);
                break;
        }
    }
*/
  /*   @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        //update header
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        // only insert if there items to insert
        if (itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
            if (expandCollapseListener != null) {
                int groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos;
                expandCollapseListener.onGroupExpanded(getGroups().get(groupIndex));
            }
        }
    }
    @Override
    public void onGroupCollapsed(int positionStart, int itemCount) {
        //update header
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        // only remote if there items to remove
        if (itemCount > 0) {
            notifyItemRangeRemoved(positionStart, itemCount);
            if (expandCollapseListener != null) {
                //minus one to return the position of the header, not first child
                int groupIndex = expandableList.getUnflattenedPosition(positionStart - 1).groupPos;
                expandCollapseListener.onGroupCollapsed(getGroups().get(groupIndex));
            }
        }
    }

   @Override
    public boolean onGroupClick(int flatPos) {
        if (groupClickListener != null) {
            groupClickListener.onGroupClick(flatPos);
        }
        return expandCollapseController.toggleGroup(flatPos);
    }

    *//**
     * @param flatPos The flat list position of the group
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     *//*
    public boolean toggleGroup(int flatPos) {
        return expandCollapseController.toggleGroup(flatPos);
    }

    *//**
     * @param group the {@link ExpandableGroup} being toggled
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     *//*
    public boolean toggleGroup(ExpandableGroup group) {
        return expandCollapseController.toggleGroup(group);
    }

    *//**
     * @param flatPos the flattened position of an item in the list
     * @return true if {@code group} is expanded, false if it is collapsed
     *//*
    public boolean isGroupExpanded(int flatPos) {
        return expandCollapseController.isGroupExpanded(flatPos);
    }

    */

    /**
     * @param group the {@link ExpandableGroup} being checked for its collapsed state
     * @return true if {@code group} is expanded, false if it is collapsed
     *//*
    public boolean isGroupExpanded(ExpandableGroup group) {
        return expandCollapseController.isGroupExpanded(group);
    }*/
    @Override
    public MonthViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_month_payment, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public FeeViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_fee, parent, false);
        return new FeeViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(FeeViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        PaymentFeeModel feeModel = ((PaymentFeeGroupModel) group).getItems().get(childIndex);
        holder.lnParent.removeAllViews();

        for (int j = 0; j < feeModel.getDueAmount().length; j++) {

            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView details = new TextView(context);
            details.setText("Amount Payable : " + feeModel.getAmountPayable()[j] + " | Amount Paid : " + feeModel.getAmountPaid()[j] + " | Due Amount : " + feeModel.getDueAmount()[j]);
            ;
            details.setPadding(0, 0, 0, 18);
            details.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            details.setTextColor(Color.WHITE);
            details.setTextSize(14);
            details.setGravity(Gravity.LEFT);


            final CustomTextView textView = new CustomTextView(context);
            textView.setText(feeModel.getFeeHead()[j] + " : ");
            textView.setPadding(0, 0, 0, 7);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(16);
            textView.setGravity(Gravity.LEFT);

            CheckBox checkBox = new CheckBox(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            checkBox.setChecked(true);
            checkBox.setClickable(false);
            checkBox.setLayoutParams(params);
            checkBox.setVisibility(View.GONE);
            relativeLayout.addView(checkBox);

            int finalJ = j;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (checkBox.isChecked()) {

                        //            Toast.makeText(context, feeModel.getMonthName()+" --"+feeModel.getFeeHead()[finalJ]+" --"+ feeModel.getDueAmount()[finalJ], Toast.LENGTH_SHORT).show();

                        //   dueAmount=dueAmount+Integer.parseInt(feeModel.getDueAmount()[finalJ]);


                        //   onClickLitener.childCheck(Integer.parseInt(feeModel.getDueAmount()[finalJ]));
                        onClickLitener.childCheck(finalJ, childIndex, feeModel.getStudentFeeStructureID()[finalJ], feeModel.getDueAmount()[finalJ]);
                    } else {
                        //   dueAmount=dueAmount-Integer.parseInt(feeModel.getDueAmount()[finalJ]);

                        //  onClickLitener.childUnCheck(Integer.parseInt(feeModel.getDueAmount()[finalJ]));
                        onClickLitener.childUnCheck(finalJ, childIndex, feeModel.getStudentFeeStructureID()[finalJ], feeModel.getDueAmount()[finalJ]);
                    }
                }
            });
      /*  ListView details = new ListView(context);
        details.setAdapter(new CustomeListView(feeModel));
        details.setPadding(0, 0, 0, 18);
        details.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
*/


            linearLayout.addView(textView);
            linearLayout.addView(details);
            relativeLayout.addView(linearLayout);


            holder.lnParent.addView(relativeLayout);
        }

    }


    @Override
    public void onBindGroupViewHolder(MonthViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.tvMonth.setText(group.getTitle());

        Log.v("SSSSSSSSS", String.valueOf(flatPosition));
        //  Log.v("SSSSSSSSSgroup",String.valueOf(group.));
        PaymentFeeGroupModel feeGroupModel = (PaymentFeeGroupModel) group;

       /* holder.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(int flatPos) {

                   if(holder.cbClick.isChecked()){
                    holder.cbClick.setChecked(false);
                    holder.collapse();
                    return false;
                }
                else {
                holder.cbClick.setChecked(true);
                 holder.expand();
                return false;
                }
            }
        });*/


        int status = feeGroupModel.getFeeStatus();
        if (status == 1) {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.app_green));
        } else if (status == -1) {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.app_red));
        } else {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.app_grey));
        }

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

               /* if(holder.cbClick.isChecked()){
                    holder.cbClick.setChecked(false);
                       return false;
                }
                else {*/
                holder.cbClick.setChecked(true);
                return false;
                /*  }*/
            }
        });

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cbClick.isChecked()){
                    holder.cbClick.setChecked(false);
                 //   return false;
                }
                else {
                    holder.cbClick.setChecked(true);
                //    return false;
                }
            }
        });*/


        holder.cbClick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //    Log.v("SSSSSSSS2222",String.valueOf(holder.get()));
                if (holder.cbClick.isChecked()) {
                    //  Toast.makeText(context, group.getTitle()+flatPosition, Toast.LENGTH_SHORT).show();

                   /* PaymentFeeModel paymentFeeModel=groups.get(flatPosition).getItems().getClass();
                    for (int j=0;j<feeModel.getDueAmount().length;j++){
                        dueAmount=dueAmount+Integer.parseInt(feeModel.getDueAmount()[j]);
                    }*/
                    //   notifyDataSetChanged();
                    onClickLitener.headerCheck(flatPosition, group.getTitle());
                    //  notifyItemChanged(flatPosition);
                    //  notifyDataSetChanged();

                } else {
                    //  Toast.makeText(context, group.getTitle()+flatPosition, Toast.LENGTH_SHORT).show();

                        /*PaymentFeeModel feeModel = ((PaymentFeeGroupModel) group).getItems().get(flatPosition);
                        for (int j=0;j<feeModel.getDueAmount().length;j++){
                            dueAmount=dueAmount-Integer.parseInt(feeModel.getDueAmount()[j]);
                        };*/
                    //    notifyDataSetChanged();
                    onClickLitener.headerUnCheck(flatPosition, group.getTitle());
                    //     notifyDataSetChanged();

                }
            }
        });
    }

    /*  private class CustomeListView extends BaseAdapter {


          PaymentFeeModel feeModel;
          LayoutInflater inflter;

          public CustomeListView(PaymentFeeModel feeModel) {

              this.feeModel = feeModel;

              inflter = (LayoutInflater.from(context));
          }

          @Override
          public int getCount() {
              return feeModel.getDueAmount().length;
          }

          @Override
          public Object getItem(int i) {
              return null;
          }

          @Override
          public long getItemId(int i) {
              return 0;
          }

          @Override
          public View getView(int i, View view, ViewGroup viewGroup) {

              view = inflter.inflate(R.layout.payment_single_item,  viewGroup, false);
              TextView tvDueAmount = (TextView) view.findViewById(R.id.tvDueAmount);

              tvDueAmount.setText(feeModel.getMonthName()+"--"+feeModel.getDueAmount()[i]);

              return view;
          }
      }*/
    public interface onClickLitener {

        void headerCheck(int postion, String title);

        void headerUnCheck(int postion, String title);

        void childCheck(int childPosition, int parentPosition, String StudentFeeStructureID, String dueAmount);

        void childUnCheck(int childPosition, int parentPosition, String StudentFeeStructureID, String dueAmount);
    }

    class MonthViewHolder extends GroupViewHolder {

        TextView tvMonth;
        CardView cardStatus;
        CheckBox cbClick;

        public MonthViewHolder(View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            cardStatus = itemView.findViewById(R.id.cardStatus);
            cbClick = itemView.findViewById(R.id.cbClick);
        }


    }

    class FeeViewHolder extends ChildViewHolder {
        /*
                TextView tvAdmission,tvAdmissionDetails,tvTution,tvTutionDetails,tvComputer,
                        tvComputerDetails,tvExam,tvExamDetails;*/
        LinearLayout lnParent;

        public FeeViewHolder(View itemView) {
            super(itemView);
           /* tvAdmission = itemView.findViewById(R.id.tvAdmission);
            tvAdmissionDetails = itemView.findViewById(R.id.tvAdmissionDetails);
            tvTution = itemView.findViewById(R.id.tvTution);
            tvTutionDetails = itemView.findViewById(R.id.tvTutionDetails);
            tvComputer = itemView.findViewById(R.id.tvComputer);
            tvComputerDetails = itemView.findViewById(R.id.tvComputerDetails);
            tvExam = itemView.findViewById(R.id.tvExam);
            tvExamDetails = itemView.findViewById(R.id.tvExamDetails);*/
            lnParent = itemView.findViewById(R.id.lnParent);
        }

    }
}
