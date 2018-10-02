package com.brainyoung.ryan.rideoff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultRunningAdapter extends BaseAdapter {
    private ArrayList<ResultRunningInfo> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ResultRunningInfo getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ResultRunningInfo myItem = getItem(position);

        // TODO : 시퀀스 번호 일치시 im_current_bus = Visible 로 설정.

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        return convertView;
    }

    public void addItem(String plateNo, String routeId, String stationId, String stationSeq) {

        ResultRunningInfo mItem = new ResultRunningInfo(plateNo, routeId, stationId, stationSeq);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }

}
