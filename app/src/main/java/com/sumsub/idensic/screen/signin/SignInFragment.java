package com.sumsub.idensic.screen.signin;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.sumsub.idensic.App;
import com.sumsub.idensic.R;
import com.sumsub.idensic.manager.PrefManager;
import com.sumsub.idensic.screen.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

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

        cvCamera = view.findViewById(R.id.camera);
        initCamera();
    }

    private void initCamera() {

        cvCamera.setLifecycleOwner(this);
        cvCamera.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(frame.getFormat(), frame.getRotationToView(), frame.getSize().getWidth(), frame.getSize().getHeight(), frame.getData());
            }
        });

    }

    private void processImage(int format, int rotation, int width, int height, byte[] data) {
        InputImage image = InputImage.fromByteArray(data, width, height, rotation, format);
        scanner.process(image).addOnSuccessListener(barcodes -> {
            for(Barcode barcode : barcodes) {
                try {
                    String json = new String(Base64.decode(barcode.getRawBytes(), Base64.NO_WRAP));
                    LoginData loginData = new Gson().fromJson(json, LoginData.class);
                    PrefManager prefManager = App.getInstance().getPrefManager();
                    prefManager.setUrl(loginData.url);
                    prefManager.setToken(loginData.t);
                    NavHostFragment.findNavController(SignInFragment.this).navigate(R.id.action_sign_in_to_main);
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        scanner.close();
        super.onDestroy();
    }

    @Override
    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }
}
