package uz.techie.shifobaxshasaluz.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.fragments.PhoneInputFragmentDirections;

public class AffiliateDialog extends Dialog {
    TextInputLayout inputLayout;
    MaskedEditText editText;
    MaterialButton btnCancel, btnConfirm;
    AffiliateDialogListener listener;


    public AffiliateDialog(Context context, AffiliateDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.dialog_input_affiliate);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        inputLayout = findViewById(R.id.phone_input_phone_container);
        editText = findViewById(R.id.phone_input_phone_edittext);
        btnCancel = findViewById(R.id.dialog_btn_cancel);
        btnConfirm = findViewById(R.id.dialog_btn_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPhone = "998"+editText.getUnmaskedText();
                if (sPhone.length()<12){
                    inputLayout.setError(getContext().getString(R.string.mavjud_telefon_raqami));
                }
                else {
                    inputLayout.setError(null);
                    dismiss();
                    listener.onConfirmClick(sPhone);
                }


            }
        });

    }

    public interface AffiliateDialogListener{
        void onConfirmClick(String phone);
    }

}
