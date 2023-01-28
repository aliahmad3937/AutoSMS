package com.auto.sms.SmsBlaster;

public class CustomModel {
    private static CustomModel mInstance;
    private OnCustomStateListener mListener;
    private boolean mState;

    public interface OnCustomStateListener {
        void stateChanged();
    }

    private CustomModel() {
    }

    public static CustomModel getInstance() {
        if (mInstance == null) {
            mInstance = new CustomModel();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener onCustomStateListener) {
        this.mListener = onCustomStateListener;
    }

    public void changeState(boolean z) {
        if (this.mListener != null) {
            this.mState = z;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return this.mState;
    }

    private void notifyStateChange() {
        this.mListener.stateChanged();
    }
}
