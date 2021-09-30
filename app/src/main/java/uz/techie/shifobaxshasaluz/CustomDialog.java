package uz.techie.shifobaxshasaluz;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import uz.nisd.asalsavdosi.R;

public class CustomDialog {

    private Activity activity;
    private AlertDialog dialog;
    private CustomDialogInterface anInterface;
    private String title = "";
    private String message = "";
    private boolean isSuccess = false;
    private int status = 0;


    public CustomDialog(Activity activity, CustomDialogInterface anInterface) {
        this.activity = activity;
        this.anInterface = anInterface;
    }

    public void title(String title){
        this.title = title;
    }
    public void message(String message){
        this.message = message;
    }
    public void isSuccess(boolean isSuccess){
        this.isSuccess = isSuccess;
    }
    public void status(int status){
        this.status = status;
    }

    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_dialog, null, false);
        builder.setView(view);
        builder.setCancelable(false);


        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        TextView tvTitle = dialog.findViewById(R.id.custom_dialog_title);
        TextView tvMessage = dialog.findViewById(R.id.custom_dialog_message);
        Button btnOk = dialog.findViewById(R.id.custom_dialog_btnOk);
        CircleImageView image = dialog.findViewById(R.id.custom_dialog_image);

        tvTitle.setText(title);
        tvMessage.setText(message);

        if (isSuccess){
            image.setImageResource(R.drawable.ic_success);
        }
        else {
            image.setImageResource(R.drawable.ic_failed);
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                anInterface.onCustomDialogBtnClick(status);
            }
        });


    }

    public void hide(){
        dialog.dismiss();
    }


    public interface CustomDialogInterface{
        void onCustomDialogBtnClick(int status);
    }


}
