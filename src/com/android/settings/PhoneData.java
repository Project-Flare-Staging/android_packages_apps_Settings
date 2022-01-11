package com.android.settings;

import java.util.ArrayList;
import java.util.List;
import com.android.settings.R;

public final class PhoneData {
	public static List<AboutPhoneData> data = new ArrayList<>();
	static {
		data.add(new AboutPhoneData(0));
		// Raphael
		data.add(new AboutPhoneData(1,
			"raphael",
			"1 x 2.84 + 3 x 2.42 GHz + 4 x 1.78",
			"Snapdragon 855 (7 nm)",
			"4000",
			"1080 x 2340, 6.39 inches",
			"48MP + 8MP + 13MP, 20MP"
		));
		// Raphaelin
		data.add(new AboutPhoneData(2,
			"raphaelin",
			"1 x 2.84 + 3 x 2.42 GHz + 4 x 1.78",
			"Snapdragon 855 (7 nm)",
			"4000",
			"1080 x 2340, 6.39 inches",
			"48MP + 8MP + 13MP, 20MP"
		));

}
    public static List<AboutPhoneData> getData() {
	return data;
    }
}
