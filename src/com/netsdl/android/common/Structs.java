package com.netsdl.android.common;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netsdl.android.main.view.Login.Status;

public class Structs implements Serializable{
	private static final long serialVersionUID = 7140676115707227296L;

	public class Item implements Serializable{
		private static final long serialVersionUID = 6751519151605241578L;

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

	public enum Type implements Serializable{
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

	public enum DocumentType implements Serializable{
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

	public enum RtnType implements Serializable{
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

	public class DeviceItem implements Serializable {
		private static final long serialVersionUID = 3344079987392960944L;
		public DeviceItem() {

		}

		public String deviceID;
		public String[] shop;
		public String[] outOfShop;
		public String[] custom;
		public String[] salesType;
		public String documentDate;
		public String[] operator;
		public String remarks;
		
		public int intStart;
		public String printWSDL;
		public String printNameSpace;
		public String printMethod;
		public String printFlag;

	}
	
	public class LoginViewData implements Serializable {
		private static final long serialVersionUID = 5956154244068859349L;
		public Status status = Status.operaterID;
		public Object[] storeObjs;
		public String text = "";
	}

}