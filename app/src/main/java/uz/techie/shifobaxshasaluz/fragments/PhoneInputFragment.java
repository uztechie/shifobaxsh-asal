package uz.techie.shifobaxshasaluz.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.PhoneInputFragmentDirections;
import uz.techie.shifobaxshasaluz.CustomDialog;
import uz.techie.shifobaxshasaluz.Utils;


public class PhoneInputFragment extends Fragment implements CustomDialog.CustomDialogInterface {
    MaskedEditText editText;
    FloatingActionButton nextBtn;
    TextInputLayout inputLayout;

    CustomDialog customDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_input, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customDialog = new CustomDialog(requireActivity(), PhoneInputFragment.this);
        editText = view.findViewById(R.id.phone_input_phone_edittext);
        nextBtn = view.findViewById(R.id.phone_input_next_btn);
        inputLayout = view.findViewById(R.id.phone_input_phone_container);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPhone = "+998"+editText.getUnmaskedText();
                if (sPhone.length()<=12){
                    inputLayout.setError(getString(R.string.mavjud_telefon_raqami));
                    return;
                }
                if (!Utils.hasInternetConnection(getContext())){
                    inputLayout.setError(null);
                    Snackbar.make(requireView(), getString(R.string.internetga_ulanmagan), Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Navigation.findNavController(view).navigate(
                            PhoneInputFragmentDirections.actionPhoneInputFragmentToCodeVerificationFragment(sPhone));
                }
            }
        });



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }




    private void retryAlertdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.internetga_boglanish);
        builder.setNegativeButton(R.string.qayta_urinish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().recreate();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    @Override
    public void onCustomDialogBtnClick(int status) {

    }
}
