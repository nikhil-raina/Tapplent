package com.example.nikhi_000.tapplent_demo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by nikhi_000 on 12/24/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<DataPerson> data;
    private List<DataPerson> selectedDatList;

    private DataPerson current;
    private int curPos;

    /**
     * 
     * @param context
     * @param data
     */
    public Adapter(Context context, List<DataPerson> data){

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.curPos = 0;
        this.selectedDatList = new ArrayList<>();
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId_listItem = R.layout.items_layout;
        View view = inflater.inflate(layoutId_listItem, parent, false);
        return new ViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        current = data.get(position);
        holder.personName.setText(current.personName);
        holder.personInfo.setText(current.personInfo);
        holder.personInitialName.setText(current.personInitialName);
        holder.parent.setBackgroundColor(Color.WHITE);
        curPos = holder.getAdapterPosition();
        holder.parent.setTag(current);
    }

    /**
     *
     * @return
     */
    public List<DataPerson> getSelectedDatList(){
        return this.selectedDatList;
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     *
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView personName;
        private TextView personInfo;
        private TextView personInitialName;
        private RelativeLayout parent;

        /**
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);

            parent = (RelativeLayout) itemView.findViewById(R.id.perContact_fullBody);
            personName = (TextView)itemView.findViewById(R.id.personName);
            personInfo = (TextView) itemView.findViewById(R.id.personInfo);
            personInitialName = (TextView) itemView.findViewById(R.id.initialName);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        /**
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if(selectedDatList.isEmpty()){
                current = data.get(getAdapterPosition());
            }
            else onLongClick(v);

        }

        /**
         *
         * @param v
         * @return
         */
        @Override
        public boolean onLongClick(View v) {
            current = data.get(getAdapterPosition());
            if(selectedDatList.contains(current)) {
                v.setBackgroundColor(Color.WHITE);
                selectedDatList.remove(current);
                return false;
            }
            else{
                v.setBackgroundColor(Color.LTGRAY);
                selectedDatList.add(current);
                return true;
            }
        }
    }
}
