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

	public enum Type {
		type1 {
			public DocumentType toDocumentType() {
				return DocumentType.DO;
			}

			public RtnType toRtnType() {
				return RtnType.P1;
			}
		},
		type2 {
			public DocumentType toDocumentType() {
				return DocumentType.DO;
			}

			public RtnType toRtnType() {
				return RtnType.M1;
			}
		},
		type3 {
			public DocumentType toDocumentType() {
				return DocumentType.RO;
			}

			public RtnType toRtnType() {
				return RtnType.M1;
			}
		};

		public DocumentType toDocumentType() {
			return null;
		}

		public RtnType toRtnType() {
			return null;
		}

	}

	public enum DocumentType {
		DO {
			public String toString() {
				return "DO";
			}
		},
		RO {
			public String toString() {
				return "RO";
			}
		};

	}

	public enum RtnType {
		P1 {
			public String toString() {
				return "1";
			}
		},
		M1 {
			public String toString() {
				return "-1";
			}
		}
	}
}
