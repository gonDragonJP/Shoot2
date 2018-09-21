package com.gondragon.shoot2.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MenuDialog extends AlertDialog {

    class DialogView extends ListView{

        public DialogView(Context context) {
            super(context);


        }
    }

    public MenuDialog(Context context) {
        super(context);

        setView(new DialogView(context));
        setTitle("Menu");
        setButton(DialogInterface.BUTTON_POSITIVE,"hoge", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        setButton(DialogInterface.BUTTON_NEGATIVE,"fuga", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void but1(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
