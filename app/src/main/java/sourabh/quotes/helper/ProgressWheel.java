package sourabh.quotes.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import sourabh.quotes.R;

public class ProgressWheel {
    Context context;
    ProgressDialog dialog;
    Activity activity;
    DrawerLayout ll ;
    View v ;LayoutInflater inflater;
    public ProgressWheel(Context context, Activity activity) {
        this.context = context;this.activity=activity;ll = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
    }

    public void ShowWheel(String title, String message) {
        this.dialog = new ProgressDialog(this.context, 0);
        this.dialog.setTitle(title);
        this.dialog.setMessage(message);
        this.dialog.setCancelable(true);
        this.dialog.show();
    }

    public void DismissWheel() {
       // this.dialog.dismiss();
        ll.removeView(v);}

    public void ShowDefaultWheel() {
//        ShowWheel("Loading", "Please Wait");
        // get your outer relative layout
         inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v= inflater.inflate(R.layout.loading, ll,false);;


            ll.addView(v); }
}
