package com.common.util;

import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ListUtiles {

	public static boolean isEmpty(ArrayList list) {
		if (null == list || list.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isAllEmpty(ArrayList l1, ArrayList l2) {
		if (isEmpty(l1) && isEmpty(l2)) {
			return true;
		}
		return false;
	}
	public static int getListSize(ArrayList list){
		return (null==list?0:list.size());
	}


}
