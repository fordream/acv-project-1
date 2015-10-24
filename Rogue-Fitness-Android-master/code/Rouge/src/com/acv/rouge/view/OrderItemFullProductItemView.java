package com.acv.rouge.view;

import com.acv.rouge.R;
import com.acv.rouge.db.ItemsTable;

import z.base.CommonAndroid;
import z.base.Fonts;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

//com.acv.rouge.view.OrderItemFulProductItemView
public class OrderItemFullProductItemView extends LinearLayout {

	public OrderItemFullProductItemView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.order_item_full_product_item, this);
	}

	public OrderItemFullProductItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(getContext(), R.layout.order_item_full_product_item, this);
	}

	public void setData(Cursor cursor) {
		CommonAndroid.setText(this, R.id.order_item_full_product_item_name, CommonAndroid.getString(cursor, ItemsTable.name));
		CommonAndroid.setText(this, R.id.order_item_full_product_item_sku, CommonAndroid.getString(cursor, ItemsTable.sku));
		CommonAndroid.setText(this, R.id.order_item_full_product_item_qty, CommonAndroid.getString(cursor, ItemsTable.quantity));

		TextView order_item_full_product_item_name = CommonAndroid.getView(this, R.id.order_item_full_product_item_name);

		CommonAndroid.setTextNormal(this, R.id.order_item_full_product_item_name);
		CommonAndroid.setTextNormal(this, R.id.order_item_full_product_item_sku);
		CommonAndroid.setTextNormal(this, R.id.order_item_full_product_item_qty);

		order_item_full_product_item_name.setPadding((int) getContext().getResources().getDimension(R.dimen.dimen_20dp), 0, 0, 0);
		if (cursor.getPosition() % 2 == 0) {
			findViewById(R.id.main).setBackgroundColor(getResources().getColor(R.color.order_prouct_1));
		} else {
			findViewById(R.id.main).setBackgroundColor(getResources().getColor(R.color.order_prouct_2));
		}
	}

}
