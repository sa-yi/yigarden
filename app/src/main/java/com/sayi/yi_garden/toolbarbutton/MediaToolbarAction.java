package com.sayi.yi_garden.toolbarbutton;


import com.sayi.yi_garden.R;

import org.wordpress.aztec.AztecTextFormat;
import org.wordpress.aztec.ITextFormat;
import org.wordpress.aztec.toolbar.IToolbarAction;
import org.wordpress.aztec.toolbar.ToolbarActionType;

import java.util.Collections;
import java.util.Set;

public enum MediaToolbarAction implements IToolbarAction {
    //GALLERY(R.id.media_bar_button_gallery, R.drawable.media_bar_button_image_multiple_selector, ToolbarActionType.OTHER, EnumSet.of(AztecTextFormat.FORMAT_NONE)),
    CAMERA(R.id.media_bar_button_camera, R.drawable.media_bar_button_camera_selector, ToolbarActionType.OTHER, Set.of(AztecTextFormat.FORMAT_NONE));

    private final int buttonId;
    private final int buttonDrawableRes;
    private final ToolbarActionType actionType;
    private final Set<ITextFormat> textFormats;

    MediaToolbarAction(int buttonId, int buttonDrawableRes, ToolbarActionType actionType, Set<ITextFormat> textFormats) {
        this.buttonId = buttonId;
        this.buttonDrawableRes = buttonDrawableRes;
        this.actionType = actionType;
        this.textFormats = textFormats;
    }

    MediaToolbarAction(int buttonId, int buttonDrawableRes, ToolbarActionType actionType) {
        this(buttonId, buttonDrawableRes, actionType, Collections.emptySet());
    }

    @Override
    public int getButtonId() {
        return buttonId;
    }

    @Override
    public int getButtonDrawableRes() {
        return buttonDrawableRes;
    }

    @Override
    public ToolbarActionType getActionType() {
        return actionType;
    }

    @Override
    public Set<ITextFormat> getTextFormats() {
        return textFormats;
    }

    @Override
    public boolean isInlineAction() {
        return false;
    }

    @Override
    public boolean isStylingAction() {
        return false;
    }
}
