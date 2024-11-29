package com.sayi.vdim.toolbarbutton;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sayi.vdim.R;

import org.wordpress.aztec.plugins.IMediaToolbarButton;
import org.wordpress.aztec.toolbar.AztecToolbar;
import org.wordpress.aztec.toolbar.IToolbarAction;

public class ImageButton implements IMediaToolbarButton {
    IMediaToolbarClickListener listener;
    AztecToolbar toolbar;
    public ImageButton(AztecToolbar toolbar){
        this.toolbar=toolbar;
    }

    @Override
    public void setMediaToolbarButtonClickListener(@NonNull IMediaToolbarClickListener iMediaToolbarClickListener) {
        listener=iMediaToolbarClickListener;
    }

    @NonNull
    @Override
    public IToolbarAction getAction() {
        return MediaToolbarAction.CAMERA;
    }

    @NonNull
    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void toggle() {
        listener.onClick(toolbar.findViewById(getAction().getButtonId()));
    }

    @Override
    public boolean matchesKeyShortcut(int i, @NonNull KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void inflateButton(@NonNull ViewGroup viewGroup) {
        LayoutInflater.from(toolbar.getContext()).inflate(R.layout.media_toobar_camera_button, viewGroup);
    }

    @Override
    public void toolbarStateAboutToChange(@NonNull AztecToolbar aztecToolbar, boolean b) {
        aztecToolbar.findViewById(getAction().getButtonId()).setEnabled(true);
    }
}
