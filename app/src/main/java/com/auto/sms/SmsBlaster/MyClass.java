package com.auto.sms.SmsBlaster;

public class MyClass {
    public  String name = null;
    public  boolean selected = false;
    public  String value = null;

    public MyClass(String str, String str2, boolean z) {
        this.value = str;
        this.name = str2;
        this.selected = z;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean z) {
        this.selected = z;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MyClass)) {
            return false;
        }
        return this.value.equals(((MyClass) obj).value);
    }
}
