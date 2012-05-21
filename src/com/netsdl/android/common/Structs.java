package com.netsdl.android.common;

import java.math.BigDecimal;

public class Structs {
	public class Item {
		public Item() {
		}

		public BigDecimal price;
		public Integer count;
		public BigDecimal lumpSum;

		public Object clone() throws CloneNotSupportedException {
			Item itemNew = new Item();

			itemNew.price = price.add(new BigDecimal("0"));
			itemNew.count = new Integer(count);
			itemNew.lumpSum = lumpSum.add(new BigDecimal("0"));
			return itemNew;
		}
	}

}
