package controller.caldroid;

import caldroid.CaldroidFragment;
import caldroid.CaldroidGridAdapter;
import caldroid.CaldroidSampleCustomAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
	}

}
