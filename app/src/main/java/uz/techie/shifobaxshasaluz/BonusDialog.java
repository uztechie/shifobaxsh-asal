package uz.techie.shifobaxshasaluz;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.models.Bonus;

public class BonusDialog {

    private final Activity activity;
    private AlertDialog dialog;
    TextView tv1,tv2,tv3,tv4,tv5, tv6;
    Button btnOk;
    CheckBox checkBox;
    ProgressBar progressBar;


    public BonusDialog(Activity activity) {
        this.activity = activity;
    }


    public void init(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_bonus, null, false);
        builder.setView(view);
        builder.setCancelable(false);


        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        checkBox = dialog.findViewById(R.id.dialog_checkbox);
        progressBar = dialog.findViewById(R.id.dialog_progressbar);
        tv1 = dialog.findViewById(R.id.txt1);
        tv2 = dialog.findViewById(R.id.txt2);
        tv3 = dialog.findViewById(R.id.txt3);
        tv4 = dialog.findViewById(R.id.txt4);
        tv5 = dialog.findViewById(R.id.txt5);
        tv6 = dialog.findViewById(R.id.txt6);
        btnOk = dialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Utils.dontShowBonusDialog();
                }
                dialog.dismiss();
            }
        });




    }
    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }


    public void show(List<Bonus> bonusList){
        dialog.show();

        for (Bonus bonus:bonusList){
            if (bonus.getId() == 1){
                tv1.setText(activity.getString(R.string.royxatdan_otganda)+" "+bonus.getAmount());
            }
            else if (bonus.getId() == 2){
                tv2.setText(activity.getString(R.string.bayramlarda)+" "+bonus.getAmount());
            }
            else if (bonus.getId() == 3){
                tv3.setText(activity.getString(R.string.bonus_taklif_qilganga)+" "+bonus.getAmount()+"%");
            }
            else if (bonus.getId() == 4){
                tv4.setText(activity.getString(R.string.bonus_xarid_qilganda)+" "+bonus.getAmount()+"%");
            }
            else if (bonus.getId() == 5){
                tv5.setText(activity.getString(R.string.tugulgan_kuningizda)+" "+bonus.getAmount());
            }
            else if (bonus.getId() == 6){
                tv6.setText(activity.getString(R.string.taklif_qilgan_dostingizga)+" "+bonus.getAmount());
            }

        }


    }





}
