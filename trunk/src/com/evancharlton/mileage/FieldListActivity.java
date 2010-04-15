package com.evancharlton.mileage;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.evancharlton.mileage.dao.Field;
import com.evancharlton.mileage.provider.FillUpsProvider;
import com.evancharlton.mileage.provider.tables.FieldsTable;

public class FieldListActivity extends BaseListActivity {
	private static final int MENU_ADD_FIELD = 0;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_ADD_FIELD, Menu.FIRST, R.string.add_field);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ADD_FIELD:
				Intent intent = new Intent(this, FieldActivity.class);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected String[] getFrom() {
		return new String[] {
				Field.TITLE,
				Field.DESCRIPTION
		};
	}

	@Override
	protected String[] getProjectionArray() {
		return FieldsTable.getFullProjectionArray();
	}

	@Override
	protected Uri getUri() {
		return Uri.withAppendedPath(FillUpsProvider.BASE_URI, FieldsTable.FIELDS_URI);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View row, int position, long id) {
		loadItem(id, FieldActivity.class);
	}
}