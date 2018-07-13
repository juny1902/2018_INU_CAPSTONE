package com.example.ryan.xmlparsing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultStationAdapter extends BaseAdapter {
    private ArrayList<ResultStationInfo> mItems = new ArrayList<>();
    CustomViewHolder holder;

    int selectedIndex = -1;

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public class CustomViewHolder {
        public TextView tv_plateNumber;
        public TextView tv_station_name;
        public TextView tv_sequence_number;
        public ImageView im_current_bus;
        public RadioButton rd_selected_bus;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ResultStationInfo getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        holder = new CustomViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_result, parent, false);
            convertView.setTag(holder);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        holder.tv_plateNumber = convertView.findViewById(R.id.tv_busPlateNumber);
        holder.tv_station_name = convertView.findViewById(R.id.tv_station_name);
        holder.tv_sequence_number = convertView.findViewById(R.id.tv_sequence_number);
        holder.im_current_bus = convertView.findViewById(R.id.im_current_bus);
        holder.rd_selected_bus = convertView.findViewById(R.id.rd_bus_check);

        if(selectedIndex == position){
            holder.rd_selected_bus.setChecked(true);
        }else{
            holder.rd_selected_bus.setChecked(false);
        }

        if(mItems.get(position).isSelected){
            convertView.setBackgroundColor(Color.parseColor("#554299E6"));
            holder.tv_station_name.setTextColor(Color.parseColor("#0C73CF"));
            holder.tv_sequence_number.setTextColor(Color.parseColor("#0C73CF"));
            holder.tv_plateNumber.setTextColor(Color.parseColor("#0C73CF"));
            holder.tv_station_name.setPaintFlags(holder.tv_station_name.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            holder.tv_sequence_number.setPaintFlags(holder.tv_sequence_number.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            holder.tv_plateNumber.setPaintFlags(holder.tv_plateNumber.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        }else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
            holder.tv_station_name.setTextColor(Color.GRAY);
            holder.tv_sequence_number.setTextColor(Color.GRAY);
            holder.tv_plateNumber.setTextColor(Color.GRAY);
            holder.tv_station_name.setPaintFlags(holder.tv_station_name.getPaintFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
            holder.tv_sequence_number.setPaintFlags(holder.tv_sequence_number.getPaintFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
            holder.tv_plateNumber.setPaintFlags(holder.tv_plateNumber.getPaintFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
        }
        if(mItems.get(position).visibility){
            holder.im_current_bus.setVisibility(View.VISIBLE);
            holder.tv_plateNumber.setVisibility(View.VISIBLE);
        }else{
            holder.im_current_bus.setVisibility(View.INVISIBLE);
            holder.tv_plateNumber.setVisibility(View.INVISIBLE);
        }
        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ResultStationInfo myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        holder.tv_station_name.setText(myItem.stationName);
        holder.tv_sequence_number.setText(myItem.stationSeq);
        holder.tv_plateNumber.setText(myItem.currentPlateNumber);

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        return convertView;
    }
    public void setBusExists(int seqNum, String plateNumber, boolean visibility){
        mItems.get(seqNum-1).visibility = visibility;
        mItems.get(seqNum-1).currentPlateNumber = plateNumber;
    }

    public void setBoldStation(int seqNum){
        mItems.get(seqNum-1).isSelected = true;
    }

    public void addItem(String stationId, String stationName, String stationSeq) {
        ResultStationInfo mItem = new ResultStationInfo(stationId, stationName, stationSeq);
        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);
    }
}
