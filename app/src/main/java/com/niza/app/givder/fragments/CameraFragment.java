package com.niza.app.givder.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niza.app.givder.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.facedetector.Rectangle;
import io.fotoapparat.facedetector.processor.FaceDetectorProcessor;
import io.fotoapparat.facedetector.view.RectanglesView;
import io.fotoapparat.view.CameraView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements Step {

    private CameraView cameraView;
    private RectanglesView rectanglesView;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FaceDetectorProcessor processor = FaceDetectorProcessor.with(getActivity()).listener(
                new FaceDetectorProcessor.OnFacesDetectedListener() {
                    @Override
                    public void onFacesDetected(List<Rectangle> faces) {

                        rectanglesView.setRectangles(faces);
                    }
                }
        ) .build();

        Fotoapparat.with(getActivity())
                .into(cameraView)
                // the rest of configuration
                .frameProcessor(processor)
                .build().start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_camera, container, false);;
        cameraView = view.findViewById(R.id.cameraView);
        rectanglesView = view.findViewById(R.id.rectanglesView);
        return view;

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
