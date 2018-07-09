package com.example.ryan.xmlparsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StationAdapter extends BaseAdapter {
    private ArrayList<StationInfo> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public StationInfo getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_station, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_stationNameList = convertView.findViewById(R.id.tv_stationNameList);
        TextView tv_regionNameList = convertView.findViewById(R.id.tv_regionNameList);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        StationInfo myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        tv_stationNameList.setText(myItem.StationName);
        tv_regionNameList.setText(myItem.RegionName);

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        return convertView;
    }

    public void addItem(String StationName, String RegionName, String StationId) {

        StationInfo mItem = new StationInfo(StationName, RegionName, StationId);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }
}
