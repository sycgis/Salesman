package com.salesman.views.citypicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.salesman.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 城市Picker
 *
 * @author zq
 */
public class CityPicker extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker counyPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;
    private Context context;
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();

    private CitycodeUtil citycodeUtil;
    private String city_code_string;
    private String city_string;

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public CityPicker(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public void setDatas(List<Cityinfo> province, HashMap<String, List<Cityinfo>> city, HashMap<String, List<Cityinfo>> couny) {
        this.province_list = province;
        this.city_map = city;
        this.couny_map = couny;
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
        provincePicker.setData(citycodeUtil.getProvince(province_list));
        provincePicker.setDefault(0);
        cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(0)));
        cityPicker.setDefault(0);
        counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(0)));
        counyPicker.setDefault(0);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    System.out.println("endselect");
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(0);
                    counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(0)));
                    counyPicker.setDefault(0);
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(id)));
                    counyPicker.setDefault(0);
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
        counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub

                if (text.equals("") || text == null)
                    return;
                if (tempCounyIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    city_code_string = citycodeUtil.getCouny_list_code().get(id);
                    int lastDay = Integer.valueOf(counyPicker.getListSize());
                    if (id > lastDay) {
                        counyPicker.setDefault(lastDay - 1);
                    }
                }
                tempCounyIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        return city_code_string;
    }

    /**
     * 返回省市区字符串
     */
    public String getCity_string() {
        if (null == provincePicker || null == cityPicker || null == counyPicker) {
            return "";
        }
        city_string = provincePicker.getSelectedText() + ";" + cityPicker.getSelectedText() + ";" + counyPicker.getSelectedText();
        return city_string;
    }

    /**
     * 返回省市区的id
     */
    public String getCity_Id() {
        if (null == citycodeUtil || null == provincePicker || null == cityPicker || null == counyPicker) {
            return "";
        }
        String cityId = citycodeUtil.getProvince_list_code().get(provincePicker.getSelectedId()) + ";"
                + citycodeUtil.getCity_list_code().get(cityPicker.getSelectedId()) + ";"
                + citycodeUtil.getCouny_list_code().get(counyPicker.getSelectedId());
        return cityId;
    }

    public interface OnSelectingListener {

        void selected(boolean selected);
    }
}
