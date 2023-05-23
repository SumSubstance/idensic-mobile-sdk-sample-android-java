package com.sumsub.idensic.screen.signin;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.otaliastudios.cameraview.CameraView;
import com.sumsub.idensic.App;
import com.sumsub.idensic.R;
import com.sumsub.idensic.manager.PrefManager;
import com.sumsub.idensic.screen.base.BaseFragment;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import timber.log.Timber;

public class SignInFragment extends BaseFragment {

    private CameraView cvCamera;
    private BarcodeScanner scanner;

    public SignInFragment() {
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        scanner = BarcodeScanning.getClient(options);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        cvCamera = view.findViewById(R.id.camera);
        initCamera();
    }

    private void initCamera() {
        cvCamera.setLifecycleOwner(this);
        cvCamera.addFrameProcessor(frame -> processImage(frame.getFormat(), frame.getRotationToView(), frame.getSize().getWidth(), frame.getSize().getHeight(), frame.getData()));

    }

    private void processImage(int format, int rotation, int width, int height, byte[] data) {
        if (scanner == null) return;

        InputImage image = InputImage.fromByteArray(data, width, height, rotation, format);
        scanner.process(image).addOnSuccessListener(barcodes -> {
            for (Barcode barcode : barcodes) {
                try {
                    String json = new String(Base64.decode(barcode.getRawValue(), Base64.NO_WRAP));
                    LoginData loginData = new Gson().fromJson(json, LoginData.class);
                    if (loginData.getUrl() != null && loginData.getT() != null) {
                        PrefManager prefManager = App.getInstance().getPrefManager();
                        prefManager.setUrl(loginData.getUrl());
                        prefManager.setToken(loginData.getT());
                        prefManager.setSandbox(loginData.isSandbox());
                        prefManager.setClientId(loginData.getClientId());
                        NavHostFragment.findNavController(SignInFragment.this).navigate(R.id.action_sign_in_to_main);
                        scanner.close();
                        scanner = null;
                    }
                } catch (Exception e) {
                    Timber.e(Log.getStackTraceString(e));
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
        super.onDestroy();
    }

    @Override
    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }
}
