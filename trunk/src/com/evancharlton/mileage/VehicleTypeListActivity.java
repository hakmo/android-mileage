package com.evancharlton.mileage;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.evancharlton.mileage.dao.VehicleType;
import com.evancharlton.mileage.provider.FillUpsProvider;
import com.evancharlton.mileage.provider.tables.VehicleTypesTable;

public class VehicleTypeListActivity extends BaseListActivity {
	private static final int MENU_CREATE = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_CREATE, Menu.NONE, R.string.add_vehicle_type);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_CREATE:
				startActivity(new Intent(this, VehicleTypeActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected String[] getFrom() {
		return new String[] {
				VehicleType.TITLE,
				VehicleType.DESCRIPTION
		};
	}

	@Override
	protected String[] getProjectionArray() {
		return VehicleTypesTable.getFullProjectionArray();
	}

	@Override
	protected Uri getUri() {
		return Uri.withAppendedPath(FillUpsProvider.BASE_URI, VehicleTypesTable.TYPES_URI);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View row, int position, long id) {
		loadItem(id, VehicleTypeActivity.class);
	}
}