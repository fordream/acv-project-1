package com.acv.rouge.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import z.base.CommonAndroid;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.acv.rouge.R;
import com.acv.rouge.db.ItemsTable;
import com.acv.rouge.db.PackagesTable;

//com.acv.rouge.view.OrderItemFulProductItemView
public class OrderItemFullView extends LinearLayout implements OnClickListener {
	private LinearLayout order_item_full_list_products;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.order_item_full_tracking) {
			if (!CommonAndroid.isBlank(carrier_url)) {
				CommonAndroid.callWeb(getContext(), carrier_url);
			}
		}
	}

	public OrderItemFullView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.order_item_full, this);
		order_item_full_list_products = CommonAndroid.getView(this, R.id.order_item_full_list_products);
		findViewById(R.id.order_item_full_tracking).setOnClickListener(this);
	}

	public OrderItemFullView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(getContext(), R.layout.order_item_full, this);
		order_item_full_list_products = CommonAndroid.getView(this, R.id.order_item_full_list_products);
	}

	public void init(Cursor cursorPackage, String location) {
		String status = CommonAndroid.getString(cursorPackage, PackagesTable.status);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("received", R.drawable.rogue_shippingdetails_process2);
		map.put("processed", R.drawable.rogue_shippingdetails_process3);
		map.put("packaged", R.drawable.rogue_shippingdetails_process4);
		map.put("quality", R.drawable.rogue_shippingdetails_process5);
		map.put("shipped", R.drawable.rogue_shippingdetails_process6);
		map.put("delivered", R.drawable.rogue_shippingdetails_process7);

		int res = R.drawable.rogue_shippingdetails_process7;
		if (map.containsKey(status + "")) {
			res = map.get(status + "");
		}

		findViewById(R.id.order_item_full_process).setBackgroundResource(res);

		carrier_url = CommonAndroid.getString(cursorPackage, PackagesTable.carrier_url);
		String trackingNumber = CommonAndroid.getString(cursorPackage, PackagesTable.trackingNumber);
		String shipping = CommonAndroid.getString(cursorPackage, PackagesTable.carrier);
		
		try {
			JSONArray array = new JSONArray(trackingNumber);
			trackingNumber = array.getString(array.length() - 1);
		} catch (Exception exception) {
			trackingNumber = "";
		}
		
		if(trackingNumber.equals("null")){
			trackingNumber = "Pending Shipment"; 
		}
		CommonAndroid.setText(this, R.id.order_item_full_tracking, trackingNumber);
		CommonAndroid.setText(this, R.id.text_1, shipping);

		CommonAndroid.setText(this, R.id.time_1, CommonAndroid.getString(cursorPackage, PackagesTable.receivedDate), "false");
		CommonAndroid.setText(this, R.id.time_2, CommonAndroid.getString(cursorPackage, PackagesTable.processedDate), "false");
		CommonAndroid.setText(this, R.id.time_3, CommonAndroid.getString(cursorPackage, PackagesTable.packedDate), "false");
		CommonAndroid.setText(this, R.id.time_4, CommonAndroid.getString(cursorPackage, PackagesTable.qualityDate), "false");
		CommonAndroid.setText(this, R.id.time_5, CommonAndroid.getString(cursorPackage, PackagesTable.pickUpDate), "false");
		CommonAndroid.setText(this, R.id.time_6, CommonAndroid.getString(cursorPackage, PackagesTable.deliveryDate), "false");

		String _idPackage = CommonAndroid.getString(cursorPackage, PackagesTable._ID);

		Cursor cursor = new ItemsTable(getContext()).querry(String.format("%s = '%s'", ItemsTable._idPackage, _idPackage));
		while (cursor != null && cursor.moveToNext()) {
			OrderItemFullProductItemView item = new OrderItemFullProductItemView(getContext());
			item.setData(cursor);
			order_item_full_list_products.addView(item);
		}

		if (cursor != null) {
			cursor.close();
		}
	}

	String carrier_url = "";
}