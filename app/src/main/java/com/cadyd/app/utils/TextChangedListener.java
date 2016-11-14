package com.cadyd.app.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import org.wcy.common.utils.StringUtil;

public class TextChangedListener implements TextWatcher {
	private boolean isChanged = false;
	EditText et;

	public TextChangedListener(EditText et) {
		this.et = et;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (isChanged) {// ----->如果字符未改变则返回
			return;
		}
		String str = s.toString();
		StringBuffer cuttedStr = new StringBuffer(str);
		isChanged = true;
		if (!StringUtil.hasText(str)) {
			
		} else if (str.equals(".") ||str.equals("00")) {
			cuttedStr.delete(0, 1);
		} else if (str.length() >= 2) {
			if (str.substring(0, 1).equals("0")) {
				cuttedStr.delete(0, 1);
			}
		}
		if (str.indexOf(".") != -1 && str.length() > 4) {
			if (str.substring(str.indexOf(".") + 1).length() > 2) {
				cuttedStr.setLength(0);
				cuttedStr.append(str.substring(0, str.indexOf(".") + 3));
			}

		} else {
			if (str.length() > 9) {
				cuttedStr.setLength(0);
				cuttedStr.append(str.substring(0, 9));
			}
		}

		et.setText(cuttedStr.toString());
		et.setSelection(et.length());
		isChanged = false;
	}
}
