package com.dev4lazy.pricecollector.view.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.dev4lazy.pricecollector.utils.DoItCallback;

/**
 * Opakowuje PopupWindow.
 * Przykład utworzenia:
 *  new PopupWindowWrapper( parentView,
 *      R.layout.remote_populate_data_popup_window ).
 *      setWidth( ViewGroup.LayoutParams.MATCH_PARENT ).
 *      setHeight( ViewGroup.LayoutParams.WRAP_CONTENT ).
 *      setGravity( Gravity.CENTER ).
 *      setOutsideTouchable( false ).
 *      setFocusable( false ).
 *      show( 0, 0 );
 *
 * Udostępnia swój widok poprzez metodę getPopupView(), dzięki czemu można się odwoływać
 * do elementów widoku.
 * Ponieważ PopupWindowWrapper implementuje metodę doIt interfesju AfterAllCallback,
 * może ona zostać wywołana w celu zamknięcia okna.
 */
public class PopupWindowWrapper implements DoItCallback {

    private View parentView;
    private View popupView;
    private PopupWindow popupWindow;
    private int gravity = Gravity.CENTER;

    public PopupWindowWrapper(View parentView, int layoutResourceId) {
        LayoutInflater inflater = (LayoutInflater) parentView.getContext().getSystemService(
                parentView.getContext().LAYOUT_INFLATER_SERVICE
        );
        this.parentView = parentView;
        popupView = inflater.inflate(layoutResourceId, null);
        popupWindow = new PopupWindow(popupView);
    }

    public PopupWindowWrapper setWidth(int width) {
        if (isPopupWindowNotNull()) {
            popupWindow.setWidth(width);
        }
        return this;
    }

    public PopupWindowWrapper setHeight(int height) {
        if (isPopupWindowNotNull()) {
            popupWindow.setHeight(height);
        }
        return this;
    }

    public PopupWindowWrapper setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public PopupWindowWrapper setFocusable(boolean focusable) {
        if (isPopupWindowNotNull()) {
            popupWindow.setFocusable(focusable);
        }
        return this;
    }

    public PopupWindowWrapper setOutsideTouchable(boolean outsideTouchable) {
        if (isPopupWindowNotNull()) {
            popupWindow.setOutsideTouchable(outsideTouchable);
        }
        return this;
    }

    private boolean isPopupWindowNotNull() {
        return popupWindow != null;
    }

    public PopupWindowWrapper show(int x, int y) {
        popupWindow.showAtLocation(parentView, gravity, x, y);
        return this;
    }

    public void close() {
        popupWindow.dismiss();
    }

    public View getPopupView() {
        return popupView;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    @Override
    public void doIt( Object... noParameters ) {
        close();
    }

}
