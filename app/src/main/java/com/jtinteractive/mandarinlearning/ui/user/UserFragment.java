package com.jtinteractive.mandarinlearning.ui.user;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.data.remote.model.LocalUser;
import com.jtinteractive.mandarinlearning.data.remote.service.SyncIntentService;
import com.jtinteractive.mandarinlearning.data.remote.service.SyncReceiver;
import com.jtinteractive.mandarinlearning.databinding.FragmentUserBinding;
import com.jtinteractive.mandarinlearning.ui.login.LoginActivity;
import com.jtinteractive.mandarinlearning.utils.ApplicationHelper;
import com.jtinteractive.mandarinlearning.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;

public class UserFragment extends Fragment implements View.OnTouchListener, IUserFragment {
    private FragmentUserBinding binding;
    private FirebaseAuth firebaseAuth;
    private UserFragmentPresenter userFragmentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userFragmentPresenter = new UserFragmentPresenter(this);
        bind();
        reloadUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetText(false, binding.currentPass, R.drawable.ic_baseline_lock_open_24, R.drawable.ic_baseline_clear_24, true);
        resetText(false, binding.newPassword, R.drawable.ic_baseline_key_24, R.drawable.ic_baseline_clear_24, true);
        resetText(false, binding.renewPassword, R.drawable.ic_baseline_key_24, R.drawable.ic_baseline_clear_24, true);
        reloadUser();
        closeUserEdit();
        closePasswordEdit();
    }


    private void bind() {
        binding.login.setOnClickListener(v -> {
            LoginActivity.starter(getContext());
        });
        binding.logout.setOnClickListener(v -> {
            userFragmentPresenter.logout();
            reloadUser();
            closeUserEdit();
            closePasswordEdit();
        });
        binding.save.setOnClickListener(v -> backup(true));
        binding.load.setOnClickListener(v -> backup(false));
        binding.cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalUser localUser = ApplicationHelper.getInstance().getLocalUser();
                if (localUser == null) {
                    return;
                }
                if (binding.editView.getVisibility() == View.VISIBLE || binding.changePassView.getVisibility() == View.VISIBLE) {
                    closeUserEdit();
                    closePasswordEdit();
                } else {
                    openUserEdit();
                }
            }
        });
        binding.name.setOnTouchListener(this);
        binding.currentPass.setOnTouchListener(this);
        binding.newPassword.setOnTouchListener(this);
        binding.renewPassword.setOnTouchListener(this);

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    resetText(true, binding.name, R.drawable.ic_outline_account_circle_24, R.drawable.ic_baseline_clear_24, true);
                } else {
                    resetText(false, binding.name, R.drawable.ic_outline_account_circle_24, 0, false);
                }
            }
        });
        binding.currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    resetText(true, binding.currentPass, R.drawable.ic_baseline_lock_open_24, R.drawable.ic_baseline_clear_24, true);
                } else {
                    resetText(false, binding.currentPass, R.drawable.ic_baseline_lock_open_24, 0, false);
                }
            }
        });

        binding.newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    resetText(true, binding.newPassword, R.drawable.ic_baseline_key_24, R.drawable.ic_baseline_clear_24, true);
                } else {
                    resetText(false, binding.newPassword, R.drawable.ic_baseline_key_24, 0, false);
                }
            }
        });

        binding.renewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    resetText(true, binding.renewPassword, R.drawable.ic_baseline_key_24, R.drawable.ic_baseline_clear_24, true);
                } else {
                    resetText(false, binding.renewPassword, R.drawable.ic_baseline_key_24, 0, false);
                }
            }
        });


        binding.saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = binding.name.getText().toString();
                if (enteredName.length() < 3 || enteredName.length() > 25) {
                    NotificationHelper.showSnackBar(binding.getRoot(), 1, "Your name must have at least 3 character or less than 25 character");
                    return;
                }
                if (!ApplicationHelper.isAlphaNumeric(enteredName)) {
                    NotificationHelper.showSnackBar(binding.getRoot(), 1, "Only character and number allowed in name");
                    return;
                }
                LocalUser localUser = ApplicationHelper.getInstance().getLocalUser();
                localUser.setName(enteredName);
                ApplicationHelper.getInstance().setLocalUser(localUser);
                userFragmentPresenter.changeName(enteredName);
                closeUserEdit();
            }
        });

        binding.changePassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.changePassView.getVisibility() == View.VISIBLE) {
                    closePasswordEdit();
                } else {
                    openPasswordEdit();
                }
            }
        });

        binding.changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFragmentPresenter.validatePasswords(binding.currentPass.getText().toString(), binding.newPassword.getText().toString(), binding.renewPassword.getText().toString());
            }
        });
    }

    private void closeUserEdit() {
        LocalUser localUser = ApplicationHelper.getInstance().getLocalUser();
        if (localUser != null) {
            binding.displayName.setVisibility(View.VISIBLE);
            binding.displayName.setText(localUser.getName());
        }
        // TransitionManager.beginDelayedTransition(binding.cardUser, new AutoTransition());
        binding.editView.setVisibility(View.GONE);
        binding.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
    }

    private void openPasswordEdit() {
        TransitionManager.beginDelayedTransition(binding.cardUser, new AutoTransition());
        binding.changePassView.setVisibility(View.VISIBLE);
    }

    private void closePasswordEdit() {
        // TransitionManager.beginDelayedTransition(binding.cardUser, new AutoTransition());
        binding.changePassView.setVisibility(View.GONE);
    }

    private void openUserEdit() {
        LocalUser localUser = ApplicationHelper.getInstance().getLocalUser();
        binding.name.setText(localUser.getName());
        binding.displayName.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.cardUser, new AutoTransition());
        binding.editView.setVisibility(View.VISIBLE);
        binding.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24);
    }

    private void resetText(boolean showClearButton, EditText input, int leftIcon, int rightIcon, boolean resetText) {
        if (showClearButton) {
            input.setCompoundDrawablesWithIntrinsicBounds(leftIcon, 0, rightIcon, 0);
            return;
        }
        input.setCompoundDrawablesWithIntrinsicBounds(leftIcon, 0, 0, 0);
        if (resetText) {
            input.setText("");
        }

        input.requestFocus();
    }


    private void backup(boolean type) {
        LocalUser currentUser = ApplicationHelper.getInstance().getLocalUser();
        if (currentUser == null) {
            // Starter
            LoginActivity.starter(getContext());
            return;
        }
        SyncIntentService.starter(getContext(), type, new SyncResultReceiver(this));
    }

    private void reloadUser() {
        LocalUser currentUser = ApplicationHelper.getInstance().getLocalUser();
        if (currentUser == null) {
            binding.displayName.setVisibility(View.GONE);
            binding.user.setText(getText(R.string.not_logged_in));
            binding.logout.setVisibility(View.GONE);
            binding.login.setVisibility(View.VISIBLE);
            return;
        }
        binding.displayName.setVisibility(View.VISIBLE);
        binding.displayName.setText(currentUser.getName());
        binding.user.setText(currentUser.getEmail());
        binding.logout.setVisibility(View.VISIBLE);
        binding.login.setVisibility(View.GONE);
    }

    public void showToast(boolean isSuccessfully) {
        if (getContext() == null) {
            Log.d(TAG, "showToast no context:");
            return;
        }
        if (isSuccessfully) {
            NotificationHelper.showSnackBar(binding.getRoot(), 0, getString(R.string.sync_success));
        } else {
            NotificationHelper.showSnackBar(binding.getRoot(), 2, getString(R.string.sync_failed));
        }
        //  Toast.makeText(getContext(), messenger, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        v.performClick();
        EditText editText = (EditText) v;
        int leftIcon = 0;
        switch (v.getId()) {
            case R.id.name:
                leftIcon = R.drawable.ic_outline_account_circle_24;
                break;
            case R.id.currentPass:
                leftIcon = R.drawable.ic_baseline_lock_open_24;
                break;
            default:
                leftIcon = R.drawable.ic_baseline_key_24;
                break;
        }
        if (editText.getCompoundDrawables()[DRAWABLE_RIGHT] == null) return false;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                resetText(false, editText, leftIcon, 0, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onValidateResult(String result) {
        if (result.length() == 0) {
            //request change password
            return;
        }
        NotificationHelper.showSnackBar(binding.getRoot(), 1, result);
    }

    @Override
    public void showSuccessful(String result) {
        NotificationHelper.showSnackBar(binding.getRoot(),0,result);
    }

    private static class SyncResultReceiver implements SyncReceiver.ResultReceiverCallBack<Integer> {
        private WeakReference<UserFragment> fragmentRef;

        public SyncResultReceiver(UserFragment fragment) {
            fragmentRef = new WeakReference<>(fragment);
        }

        @Override
        public void onSuccess(Integer data) {
            if (fragmentRef != null && fragmentRef.get() != null) {
                fragmentRef.get().showToast(true);
            }
        }

        @Override
        public void onError(Exception exception) {
            fragmentRef.get().showToast(false);
        }
    }

}

interface IUserFragment {
    void onValidateResult(String result);

    void showSuccessful(String result);

}