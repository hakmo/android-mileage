package com.evancharlton.mileage.charts;

import android.database.Cursor;

import com.artfulbits.aiCharts.Base.ChartPoint;
import com.artfulbits.aiCharts.Base.ChartPointCollection;
import com.artfulbits.aiCharts.Base.ChartSeries;
import com.artfulbits.aiCharts.Types.ChartTypes;
import com.evancharlton.mileage.ChartActivity;
import com.evancharlton.mileage.dao.Fillup;
import com.evancharlton.mileage.dao.Vehicle;
import com.evancharlton.mileage.provider.tables.FillupsTable;
import com.evancharlton.mileage.provider.tables.VehiclesTable;

public abstract class LineChart extends ChartActivity {
	protected abstract String getAxisTitle();

	protected abstract ChartGenerator createChartGenerator();

	protected abstract void processCursor(LineChartGenerator generator, ChartPointCollection points, Cursor fillups, Vehicle vehicle);

	protected final Vehicle getVehicle() {
		Cursor cursor = managedQuery(VehiclesTable.BASE_URI, VehiclesTable.PROJECTION, Vehicle._ID + " = ?", new String[] {
			getIntent().getStringExtra(VEHICLE_ID)
		}, null);
		return new Vehicle(cursor);
	}

	@Override
	protected final Object serializeData() {
		return getChart().getSeries().get(0).getPoints().toArray();
	}

	@Override
	protected final void unserializeData(Object saved) {
		ChartPoint[] savedData = (ChartPoint[]) saved;
		if (savedData != null) {
			ChartSeries series = new ChartSeries(getAxisTitle().toString(), ChartTypes.Line);
			ChartPointCollection points = series.getPoints();
			for (ChartPoint point : savedData) {
				points.add(point);
			}
			addChartSeries(series);
		}
	}

	@Override
	protected final Object[] getExecuteParameters() {
		return null;
	}

	protected ChartSeries createSeries() {
		return new ChartSeries(getAxisTitle(), ChartTypes.Line);
	}

	protected static class LineChartGenerator extends ChartGenerator {
		private final LineChart mActivity;
		private final Vehicle mVehicle;
		private final String[] mProjection;

		public LineChartGenerator(LineChart chartActivity, Vehicle vehicle, String[] projection) {
			mActivity = chartActivity;
			mVehicle = vehicle;
			mProjection = projection;
		}

		@Override
		protected ChartSeries[] doInBackground(Object... params) {
			ChartSeries series = mActivity.createSeries();
			ChartPointCollection points = series.getPoints();

			Cursor cursor = getActivity().getContentResolver().query(FillupsTable.BASE_URI, mProjection, Fillup.VEHICLE_ID + " = ?", new String[] {
				String.valueOf(mVehicle.getId())
			}, Fillup.ODOMETER + " asc");
			publishProgress(0, cursor.getCount());
			cursor.moveToFirst();
			mActivity.processCursor(this, points, cursor, mVehicle);
			cursor.close();

			if (isCancelled()) {
				return null;
			}
			return new ChartSeries[] {
				series
			};
		}

		public final void update(int update) {
			publishProgress(update);
		}
	}
}